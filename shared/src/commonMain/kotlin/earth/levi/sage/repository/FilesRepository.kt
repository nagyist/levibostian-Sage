package earth.levi.sage.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import earth.levi.sage.db.Folder
import earth.levi.sage.db.SageDatabase
import earth.levi.sage.service.HostingService
import earth.levi.sage.kotlin_inline.Result
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface FilesRepository {
    fun observeFoldersAtPath(path: String): Flow<List<Folder>>
    suspend fun updateFolderContentsFromRemote(path: String): Result<GetFolderContentsResult>
}

/**
 * Note for Android users:
 * For Dropbox, because we are using the dropbox SDK, anytime that you do a fetch, make sure it's in onResume().
 * There have been issues when doing a fetch before onResume() > login to dropbox account > auth token doesn't get saved to app. Then, you need to re-login every single time you open the app.
 */
class FilesRepositoryImpl(
    private val db: SageDatabase,
    private val hostingService: HostingService): FilesRepository {

    override fun observeFoldersAtPath(path: String): Flow<List<Folder>> {
        return db.folderQueries.selectAll().asFlow().mapToList()
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