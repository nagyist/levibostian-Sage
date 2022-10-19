package earth.levi.sage.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import earth.levi.sage.db.Folder
import earth.levi.sage.db.SageDatabase
import earth.levi.sage.service.HostingService
import earth.levi.sage.kotlin_inline.Result
import earth.levi.sage.type.LocalPhoto
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.time.Duration

interface FilesRepository {
    fun observeFoldersAtPath(path: String): Flow<List<Folder>>
    suspend fun updateFolderContentsFromRemote(path: String): Result<GetFolderContentsResult>
    suspend fun importLocalPhoto(localPhoto: LocalPhoto)
    suspend fun importLocalPhotos(localPhotos: List<LocalPhoto>)

    /**
     * Perform a sync between local device and hosting service.
     */
    suspend fun sync(): SyncResult

    /**
     * Result from performing [sync].
     */
    sealed class SyncResult {
        /// Sync has not yet started. No state.
        object None: SyncResult()
        /// Cannot reach the hosting service.
        object ConnectionError: SyncResult()
        /// User of app needs to tell us what directory in dropbox houses photos.
        object NoRootDirectorySetPhotos: SyncResult()
        /// Need to login for first time or re-login to hosting service. Got this error from a 401 from Dropbox.
        object Unauthorized: SyncResult()
        /// Not enough granted permissions to perform action.
        object NeedsPermissions: SyncResult()
        /// Success! But, the sync might have failed sometime in the process.
        data class Success(val fullSyncCompleted: Boolean): SyncResult()

        fun fold(
            connectionError: () -> Unit,
            needsPermission: () -> Unit,
            noRootDirectorySetPhotos: () -> Unit,
            none: () -> Unit,
            success: (fullSyncCompleted: Boolean) -> Unit,
            unauthorized: () -> Unit
        ) {
            when (this) {
                ConnectionError -> connectionError()
                NeedsPermissions -> needsPermission()
                NoRootDirectorySetPhotos -> noRootDirectorySetPhotos()
                None -> none()
                is Success -> success(fullSyncCompleted)
                Unauthorized -> unauthorized()
            }
        }
    }
}

/**
 * Note for Android users:
 * For Dropbox, because we are using the dropbox SDK, anytime that you do a fetch, make sure it's in onResume().
 * There have been issues when doing a fetch before onResume() > login to dropbox account > auth token doesn't get saved to app. Then, you need to re-login every single time you open the app.
 */
class FilesRepositoryImpl(
    private val db: SageDatabase,
    private val hostingService: HostingService): FilesRepository {

    override suspend fun sync(): FilesRepository.SyncResult {
        delay(Duration.parse("3s")) // to emulate network

        return FilesRepository.SyncResult.Unauthorized
    }

    override fun observeFoldersAtPath(path: String): Flow<List<Folder>> {
        return db.folderQueries.selectAll().asFlow().mapToList()
    }

    override suspend fun importLocalPhoto(localPhoto: LocalPhoto) {
        this.importLocalPhotos(listOf(localPhoto))
    }

    override suspend fun importLocalPhotos(localPhotos: List<LocalPhoto>) {
        localPhotos.forEach { localPhoto ->
            db.fileQueries.insert(
                name = localPhoto.id,
                path = localPhoto.localPath
            )
        }
    }

    override suspend fun updateFolderContentsFromRemote(path: String): Result<GetFolderContentsResult> {
        val fetchResult = hostingService.getFolderContents(path)

        fetchResult.getOrNull()?.let {
            when (it) {
                is GetFolderContentsResult.Success -> {
                    it.contents.subfolders.forEach { folder ->
                        db.folderQueries.insert(
                            name = folder.name,
                            path = folder.path
                        )
                    }
                    it.contents.files.forEach { file ->
                        db.fileQueries.insert(
                            name = file.name,
                            path = file.path
                        )
                    }
                }
                else -> {}
            }
        }

        return fetchResult
    }

}