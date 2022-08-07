package earth.levi.sage.android

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.di.filesViewModel
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.view.adapter.FolderRecyclerViewAdapter
import earth.levi.sage.android.view.adapter.PhotoRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.di.contentResolver
import earth.levi.sage.di.localPhotoStore
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val folderRecyclerViewAdapter = FolderRecyclerViewAdapter()
    private val photoRecylerViewAdapter = PhotoRecyclerViewAdapter()

    private val filesViewModel by viewModelDiGraph {
        DiGraph.filesViewModel
    }

    private val localPhotoStore = DiGraph.localPhotoStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.albums_recyclerview).apply {
            adapter = photoRecylerViewAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 4)
        }

        lifecycleScope.launch {
            // hard-coded photo path for now. that way we are guaranteed to find only photos instead of having to browse folders, too.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filesViewModel.albumsForPath("/Photos").collect { folders ->
                    //folderRecyclerViewAdapter.submitList(folders)
                }
            }
        }

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                getAndShowLocalPhotos()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                getAndShowLocalPhotos()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.

            // showInContextUI(...)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun getAndShowLocalPhotos() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoRecylerViewAdapter.submitList(localPhotoStore.fetchLocalPhotos())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            filesViewModel.updateFolderContentsFromRemote(path = "/Photos").fold(
                onSuccess = {
                    when (it) {
                        is GetFolderContentsResult.Success -> {
                            // we can show a successful message in UI for a successful sync
                        }
                        GetFolderContentsResult.Unauthorized -> {
                            filesViewModel.loginToHostingService(this@MainActivity)
                        }
                    }
                },
                onFailure = {
                    // no fetch happened at all.
                    // we can display an error message in the UI if we want.
                }
            )
        }
    }
}
