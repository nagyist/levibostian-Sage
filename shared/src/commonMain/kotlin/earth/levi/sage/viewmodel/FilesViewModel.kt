package earth.levi.sage.viewmodel

import earth.levi.sage.repository.FilesRepository
import earth.levi.sage.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilesViewModel(
    private val filesRepository: FilesRepository,
    private val logger: Logger
): ViewModel() {

    private val mutableSyncState = MutableStateFlow<FilesRepository.SyncResult>(FilesRepository.SyncResult.None)
    val syncState: StateFlow<FilesRepository.SyncResult> = mutableSyncState

    fun startSync() {
        viewModelScope.launch {
            mutableSyncState.emit(filesRepository.sync())
        }
    }

    override fun onCleared() {
        logger.debug("Clearing ${this::class.simpleName}")
    }
}