package earth.levi.sage.android.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.levi.sage.db.Folder
import earth.levi.sage.repository.FilesRepository
import earth.levi.sage.service.AndroidHostingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilesViewModel(
    private val filesRepository: FilesRepository,
    private val hostingService: AndroidHostingService
): ViewModel() {

    fun albumsForPath(path: String): Flow<List<Folder>> = filesRepository.observeFoldersAtPath(path)

    suspend fun updateFolderContentsFromRemote(path: String) = withContext(Dispatchers.IO) {
        filesRepository.updateFolderContentsFromRemote(path)
    }

    fun loginToHostingService(context: Context) {
        hostingService.login(context)
    }
}