package earth.levi.sage.android.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import earth.levi.sage.android.viewmodel.FilesViewModel
import earth.levi.sage.di.DiGraph
import earth.levi.sage.di.androidHostingService
import earth.levi.sage.di.filesRepository

val DiGraph.filesViewModel: FilesViewModel
    get() = FilesViewModel(filesRepository, androidHostingService)

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
