package earth.levi.sage.android.viewmodel


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.levi.sage.db.Folder
import earth.levi.sage.repository.FilesRepository
import earth.levi.sage.service.AndroidHostingService
import earth.levi.sage.store.LocalPhotosStore
import earth.levi.sage.type.Photo
import earth.levi.sage.type.RuntimePermission
import earth.levi.sage.util.PermissionUtil
import earth.levi.sage.util.SamplePhotosUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class DevicePhotosViewModel(
    private val permissionUtil: PermissionUtil,
    private val localPhotosStore: LocalPhotosStore,
    private val samplePhotosUtil: SamplePhotosUtil
): ViewModel() {

    private val _devicePhotos = MutableStateFlow(emptyList<Photo>())
    val devicePhotos: StateFlow<List<Photo>> = _devicePhotos

    private val _localPhotosPermission = MutableStateFlow(LocalFilesPermissionResult())
    val localPhotosPermission: StateFlow<LocalFilesPermissionResult> = _localPhotosPermission

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val _selectedPhoto = MutableStateFlow<Photo?>(null)
    val observeSelectedPhoto: StateFlow<Photo?> = _selectedPhoto
    var selectedPhoto: Photo?
        get() = _selectedPhoto.value
        set(value) { _selectedPhoto.value = value }

    private lateinit var fragment: WeakReference<Fragment>

    // this probably wont work because we are sharing viewmodel instance between multiple fragments.....
    fun initialize(fragment: Fragment) {
        this.fragment = WeakReference(fragment)

        checkPermissionStatus()

        requestPermissionLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            checkPermissionStatus()
        }
    }

    fun fetchLocalPhotos() {
        viewModelScope.launch {
            _devicePhotos.value = localPhotosStore.fetchLocalPhotos()
        }
    }

    fun fetchSamplePhotos() {
        _devicePhotos.value = samplePhotosUtil.getSamplePhotos(randomOrder = true)
    }

    fun checkPermissionStatus() {
        fragment.get()?.let { fragment ->
            val hasNeverAsked = fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            val hasPermission = permissionUtil.doesHavePermission(fragment.requireContext(), RuntimePermission.ACCESS_PHOTOS)

            _localPhotosPermission.value = LocalFilesPermissionResult(hasNeverAsked = hasNeverAsked, hasPermission = hasPermission)
        }
    }

    fun openAppSettings() {
        fragment.get()?.let { fragment ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:${fragment.requireContext().packageName}")
            fragment.startActivity(intent)
        }
    }

    fun askPermissionLocalFiles() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    data class LocalFilesPermissionResult(
        val hasNeverAsked: Boolean = true,
        val hasPermission: Boolean = false
    )
}