package earth.levi.sage.android

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import earth.levi.sage.android.di.filesViewModel
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.fragment.CloudPhotosFragment
import earth.levi.sage.android.fragment.DevicePhotosFragment
import earth.levi.sage.android.view.adapter.FolderRecyclerViewAdapter
import earth.levi.sage.android.view.adapter.PhotoRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.di.contentResolver
import earth.levi.sage.di.localPhotoStore
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    enum class OwnedFragment {
        DEVICE_PHOTOS,
        CLOUD_PHOTOS;

        fun getFragment(): Fragment {
            return when (this) {
                DEVICE_PHOTOS -> DevicePhotosFragment()
                CLOUD_PHOTOS -> CloudPhotosFragment()
            }
        }
    }

    private var setCurrentFragment: OwnedFragment = OwnedFragment.DEVICE_PHOTOS
        set(value) {
            field = value

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, value.getFragment())
                .commit()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).apply {
            setOnItemSelectedListener { menuItem ->
                setCurrentFragment = when (menuItem.itemId) {
                    R.id.action_local_images -> OwnedFragment.DEVICE_PHOTOS
                    R.id.action_cloud_images -> OwnedFragment.CLOUD_PHOTOS
                    else -> return@setOnItemSelectedListener  false
                }

                return@setOnItemSelectedListener true
            }
        }

        setCurrentFragment = OwnedFragment.DEVICE_PHOTOS
    }
}
