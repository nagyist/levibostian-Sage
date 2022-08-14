package earth.levi.sage.android.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import earth.levi.sage.android.viewmodel.CloudFilesViewModel
import earth.levi.sage.android.viewmodel.DevicePhotosViewModel
import earth.levi.sage.di.*

val DiGraph.cloudFilesViewModel: CloudFilesViewModel
    get() = CloudFilesViewModel(filesRepository, androidHostingService)

val DiGraph.devicePhotosViewModel: DevicePhotosViewModel
    get() = DevicePhotosViewModel(permissionUtil, localPhotoStore, samplePhotosUtil)

inline fun <reified VM : ViewModel> ComponentActivity.viewModelDiGraph(
    noinline createInstance: (() -> VM)
): Lazy<VM> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return createInstance() as T
            }
        }
    }
}

inline fun <reified VM : ViewModel> Fragment.viewModelDiGraph(
    noinline createInstance: (() -> VM)
): Lazy<VM> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return createInstance() as T
            }
        }
    }
}

inline fun <reified VM : ViewModel> Fragment.sharedActivityViewModelDiGraph(
    noinline createInstance: (() -> VM)
): Lazy<VM> {
    return activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return createInstance() as T
            }
        }
    }
}
