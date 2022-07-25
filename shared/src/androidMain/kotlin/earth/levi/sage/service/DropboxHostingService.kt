package earth.levi.sage.service

import android.content.Context
import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.InvalidAccessTokenException
import com.dropbox.core.oauth.DbxCredential
import earth.levi.sage.db.File
import earth.levi.sage.store.KeyValueStore
import earth.levi.sage.store.KeyValueStoreKeys.DROPBOX_ACCESS_TOKEN
import earth.levi.sage.util.Logger
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.ListFolderResult
import com.dropbox.core.v2.files.Metadata
import earth.levi.sage.Secrets
import earth.levi.sage.db.Folder
import earth.levi.sage.type.FolderContents
import earth.levi.sage.kotlin_inline.Result
import earth.levi.sage.type.error.HostingServiceGenericFetchError
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// each platform might have unique functions in it.
interface AndroidHostingService: HostingService {
    fun login(context: Context)
}

class DropboxHostingService(
    private val keyValueStore: KeyValueStore,
    private val logger: Logger): AndroidHostingService {

    private val nameOfService = "Dropbox"

    override fun login(context: Context) {
        // TODO the identifier
        val clientIdentifier = "foo.foo.foo/1.1.1"
        val requestConfig = DbxRequestConfig(clientIdentifier)

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

        Auth.startOAuth2PKCE(context, Secrets.dropboxAppKey, requestConfig, scopes)
    }

    override suspend fun getFolderContents(path: String): Result<GetFolderContentsResult> {
        logger.debug("Request to get contents of folder from $nameOfService. path: $path")

        // TODO check internet connection. result.failure if bad internet.

        // find locally saved credential, or fetch the result from the AuthActivity or, fail so app can request to login from user.
        val credential = getLocalCredential() ?: Auth.getDbxCredential() ?: return Result.success(GetFolderContentsResult.Unauthorized)

        saveCredentialLocally(credential) // save in case we just got from Auth object and have never saved locally before.

        logger.debug("Fetching contents of folder from $nameOfService. path: $path")

        // TODO change package name and version to that of the app.
        val clientIdentifier = "earth.levi.sage/1.0"
        val requestConfig = DbxRequestConfig(clientIdentifier)

        val dropboxClient = DbxClientV2(requestConfig, credential)

        return suspendCoroutine { continuation ->
            continuation.resume(getFilesForFolder(dropboxClient, path))
        }
    }

    private fun getFilesForFolder(client: DbxClientV2, folderPath: String): Result<GetFolderContentsResult> {
        fun getContentsFromResponse(entries: List<Metadata>): FolderContents {
            val subfolders = entries.map {
                val folderMetadata = it as? FolderMetadata ?: return@map null
                return@map Folder(
                        name = folderMetadata.name,
                        path = folderMetadata.pathDisplay
                    )
            }.filterNotNull()

            val files = entries.map {
                val fileMetadata = it as? FileMetadata ?: return@map null
                return@map File(
                    name = fileMetadata.name,
                    path = fileMetadata.pathDisplay
                )
            }.filterNotNull()

            return FolderContents(subfolders = subfolders, files = files)
        }

        var accumulativeFolderContents = FolderContents()

        var response: Result<ListFolderResult> = try {
            Result.success(client.files().listFolder(folderPath))
        } catch (e: InvalidAccessTokenException) {
            return Result.success(GetFolderContentsResult.Unauthorized)
        } catch (exception: DbxException) {
            logger.error(exception)
            return Result.failure(HostingServiceGenericFetchError())
        }

        response.fold(
            onSuccess = {
                var hasMore = it.hasMore
                var cursor = it.cursor

                accumulativeFolderContents = accumulativeFolderContents.addFolderContents(getContentsFromResponse(it.entries))

                while (hasMore) {
                    response = try {
                        Result.success(client.files().listFolderContinue(cursor))
                    } catch (e: InvalidAccessTokenException) {
                        return Result.success(GetFolderContentsResult.Unauthorized)
                    } catch (exception: DbxException) {
                        logger.error(exception)
                        return Result.failure(HostingServiceGenericFetchError())
                    }

                    response.fold(
                        onSuccess = {
                            hasMore = it.hasMore
                            cursor = it.cursor

                            accumulativeFolderContents = accumulativeFolderContents.addFolderContents(getContentsFromResponse(it.entries))
                        },
                        onFailure = {
                            return Result.success(GetFolderContentsResult.Success(
                                accumulativeFolderContents,
                                fullSyncCompleted = false
                            ))
                        }
                    )
                }

                return Result.success(GetFolderContentsResult.Success(
                    accumulativeFolderContents,
                    fullSyncCompleted = true
                ))
            },
            onFailure = {
                return Result.success(GetFolderContentsResult.Success(
                    accumulativeFolderContents,
                    fullSyncCompleted = false
                ))
            }
        )
    }

    private fun getLocalCredential(): DbxCredential? {
        val serializedCredential = keyValueStore.getStringOrNull(DROPBOX_ACCESS_TOKEN) ?: return null
        return DbxCredential.Reader.readFully(serializedCredential)
    }

    private fun saveCredentialLocally(dbxCredential: DbxCredential) {
        keyValueStore.putString(DROPBOX_ACCESS_TOKEN, dbxCredential.toString())
    }

}