package earth.levi.sage.service

import cocoapods.ObjectiveDropboxOfficial.*
import earth.levi.sage.Secrets
import earth.levi.sage.db.File
import earth.levi.sage.db.Folder
import earth.levi.sage.type.FolderContents
import earth.levi.sage.type.error.HostingServiceGenericFetchError
import earth.levi.sage.type.result.GetFolderContentsResult
import earth.levi.sage.util.Logger
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import kotlin.coroutines.resume
import earth.levi.sage.kotlin_inline.Result
import earth.levi.sage.type.HostingServicePermissionScope
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.darwin.dispatch_async
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.suspendCoroutine
import kotlin.native.ref.WeakReference

// this interface contains functions that are unique only to iOS.
interface iOSHostingService: HostingService {
    /**
     * Call on app startup
     */
    fun initialize()

    fun authorize(scopes: List<HostingServicePermissionScope>, application: UIApplication, dropboxLoginViewController: UIViewController)
    /**
     * Call in your app's deep link handler
     */
    fun handleUrlOpened(url: NSURL): Boolean
}

class DropboxHostingService(private val logger: Logger): iOSHostingService {

    override fun initialize() {
        // sdk throws exception if setting up app key 2+ times. Only do it once.
        if (DBClientsManager.appKey() == null) DBClientsManager.setupWithAppKey(Secrets.dropboxAppKey)
    }

    override fun authorize(
        scopes: List<HostingServicePermissionScope>,
        application: UIApplication,
        dropboxLoginViewController: UIViewController
    ) {
        val scopeRequest = DBScopeRequest(DBScopeTypeUser, getDropboxScopesFromGenericScopes(scopes), includeGrantedScopes = true)

        DBClientsManager.authorizeFromControllerV2(
            sharedApplication = application,
            controller = dropboxLoginViewController,
            loadingStatusDelegate = null,
            openURL = { url ->
                // the dropbox SDK asks you to open URLs how you wish.
                url?.let { application.openURL(it) }
            },
            scopeRequest = scopeRequest
        )
    }

    override fun handleUrlOpened(url: NSURL): Boolean {
        return DBClientsManager.handleRedirectURL(url) { authResult ->
            if (authResult != null) {
                if (authResult.isSuccess()) return@handleRedirectURL // User logged in!
                if (authResult.isCancel()) return@handleRedirectURL
                if (authResult.isError()) return@handleRedirectURL
            }
        }
    }

    override suspend fun getFolderContents(path: String): Result<GetFolderContentsResult> {
        val client = DBClientsManager.authorizedClient() ?: return Result.success(GetFolderContentsResult.Unauthorized)

        return suspendCoroutine { coroutine ->
            fun getContentsFromResponse(entries: List<DBFILESMetadata>): FolderContents {
                val files = entries.map {
                    val folderMetadata = it as? DBFILESFileMetadata ?: return@map null
                    return@map File(
                        name = folderMetadata.name,
                        path = folderMetadata.pathDisplay!!
                    )
                }.filterNotNull()

                val subfolders = entries.map {
                    val fileMetadata = it as? DBFILESFolderMetadata ?: return@map null
                    return@map Folder(
                        name = fileMetadata.name,
                        path = fileMetadata.pathDisplay!!
                    )
                }.filterNotNull()

                // Note: entry can also be of type DBFILESDeletedMetadata

                return FolderContents(subfolders = subfolders, files = files)
            }

            client.filesRoutes.listFolder(path).setResponseBlock { result, routeError, networkError ->
                routeError ?: networkError?.let {
                    (it as? Throwable)?.let {
                        logger.error(it)
                        coroutine.resume(Result.failure(HostingServiceGenericFetchError()))
                    }
                }

                var accumulatedFolderContents = FolderContents()

                var response = result as DBFILESListFolderResult

                var hasMore = response.hasMore.boolValue
                var cursor = response.cursor

                accumulatedFolderContents =
                    accumulatedFolderContents.addFolderContents(getContentsFromResponse(response.entries as List<DBFILESMetadata>))

                while (hasMore) {
                    client.filesRoutes.listFolderContinue(cursor).setResponseBlock { result, routeError, networkError ->
                            routeError ?: networkError?.let {
                                (it as? Throwable)?.let {
                                    logger.error(it)

                                    coroutine.resume(
                                        Result.success(
                                            GetFolderContentsResult.Success(
                                                contents = accumulatedFolderContents,
                                                fullSyncCompleted = false
                                            )
                                        )
                                    )
                                }
                            }

                            response = result as DBFILESListFolderResult

                            hasMore = response.hasMore.boolValue
                            cursor = response.cursor

                            accumulatedFolderContents = accumulatedFolderContents.addFolderContents(
                                getContentsFromResponse(response.entries as List<DBFILESMetadata>)
                            )
                        }
                }

                coroutine.resume(
                    Result.success(
                        GetFolderContentsResult.Success(
                            contents = accumulatedFolderContents,
                            fullSyncCompleted = true
                        )
                    )
                )
            }
        }
    }

    private fun getDropboxScopesFromGenericScopes(scopes: List<HostingServicePermissionScope>): List<String> {
        // The scope's your app will need from Dropbox
        // Read more about Scopes here: https://developers.dropbox.com/oauth-guide#dropbox-api-permissions
        return scopes.flatMap { genericScope ->
            when (genericScope) {
                HostingServicePermissionScope.READ_FILES -> listOf(
                    "files.metadata.read",
                    "files.content.read",
                )
                HostingServicePermissionScope.WRITE_FILES -> listOf(
                    "files.metadata.write",
                    "files.content.write"
                )
                HostingServicePermissionScope.SHARE_FILES -> listOf(
                    "sharing.read",
                    "sharing.write"
                )
            }
        }
    }

}
