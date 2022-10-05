package earth.levi.sage.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import earth.levi.sage.android.di.devicePhotosViewModel
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.fragment.CloudPhotosFragment
import earth.levi.sage.android.fragment.DevicePhotosFragment
import earth.levi.sage.android.fragment.PhotoFragment
import earth.levi.sage.di.DiGraph
import earth.levi.sage.type.Photo
import kotlinx.coroutines.flow.collect
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

    private val devicePhotosViewModel by viewModelDiGraph {
        DiGraph.devicePhotosViewModel
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

        findViewById<TabLayout>(R.id.tab_layout).apply {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    setCurrentFragment = when (tab.text) {
                        getString(R.string.device_images) -> OwnedFragment.DEVICE_PHOTOS
                        getString(R.string.cloud_images) -> OwnedFragment.CLOUD_PHOTOS
                        else -> return
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                devicePhotosViewModel.observeSelectedPhoto.collect { it?.let { selectedPhoto ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PhotoFragment())
                        .addToBackStack(null)
                        .commit()
                }}
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setCurrentFragment = OwnedFragment.DEVICE_PHOTOS
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.fragment_container)
        (fragment as? OnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}