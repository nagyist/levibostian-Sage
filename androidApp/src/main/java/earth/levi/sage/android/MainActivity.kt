package earth.levi.sage.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import earth.levi.sage.android.fragment.CloudPhotosFragment
import earth.levi.sage.android.fragment.DevicePhotosFragment

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
    }

    override fun onStart() {
        super.onStart()

        setCurrentFragment = OwnedFragment.DEVICE_PHOTOS
    }
}