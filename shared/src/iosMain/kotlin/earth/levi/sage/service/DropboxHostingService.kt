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
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.darwin.dispatch_async
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.suspendCoroutine

interface iOSHostingService: HostingService {
    fun initialize()
    fun login(application: UIApplication, viewController: UIViewController)
    fun handleUrlOpened(url: NSURL): Boolean
}

class DropboxHostingService(private val logger: Logger): iOSHostingService {

    override fun initialize() {
        // sdk throws exception if setting up app key 2+ times
        if (DBClientsManager.appKey() == null) DBClientsManager.setupWithAppKey(Secrets.dropboxAppKey)
    }

    override fun login(application: UIApplication, viewController: UIViewController) {
        // The scope's your app will need from Dropbox
        // Read more about Scopes here: https://developers.dropbox.com/oauth-guide#dropbox-api-permissions
        val scopes = listOf(
            "account_info.read",
            "files.metadata.read",
            "files.metadata.write",
            "files.content.read",
            "files.content.write",
            "sharing.read",
            "sharing.write"
        )

        val scopeRequest = DBScopeRequest(DBScopeTypeUser, scopes, includeGrantedScopes = true)

        DBClientsManager.authorizeFromControllerV2(
            sharedApplication = application,
            controller = viewController,
            loadingStatusDelegate = null,
            openURL = { url ->
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

}
