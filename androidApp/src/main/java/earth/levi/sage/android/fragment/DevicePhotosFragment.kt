package earth.levi.sage.android.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.R
import earth.levi.sage.android.di.filesViewModel
import earth.levi.sage.android.di.sharedActivityViewModelDiGraph
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.view.adapter.PhotoRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.di.localPhotoStore
import kotlinx.coroutines.launch

class DevicePhotosFragment: Fragment() {

    private val photoRecylerViewAdapter = PhotoRecyclerViewAdapter()

    private val filesViewModel by sharedActivityViewModelDiGraph {
        DiGraph.filesViewModel
    }

    private val localPhotoStore = DiGraph.localPhotoStore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recyclerview).apply {
            adapter = photoRecylerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
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
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
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
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
}