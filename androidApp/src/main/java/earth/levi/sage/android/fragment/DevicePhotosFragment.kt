package earth.levi.sage.android.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.R
import earth.levi.sage.android.di.devicePhotosViewModel
import earth.levi.sage.android.di.sharedActivityViewModelDiGraph
import earth.levi.sage.android.widget.CTAButtonView
import earth.levi.sage.android.view.adapter.PhotoRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.type.Photo
import kotlinx.coroutines.launch

class DevicePhotosFragment: Fragment() {

    private val photoRecylerViewAdapter = PhotoRecyclerViewAdapter().also {
        it.onItemClick = { position, photo ->
            deviceFilesViewModel.selectedPhoto = photo
        }
    }

    private val deviceFilesViewModel by sharedActivityViewModelDiGraph {
        DiGraph.devicePhotosViewModel
    }

    private lateinit var ctaButtonView: CTAButtonView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceFilesViewModel.initialize(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recyclerview).apply {
            adapter = photoRecylerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        ctaButtonView = view.findViewById(R.id.cta_view)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                deviceFilesViewModel.checkPermissionStatus()

                deviceFilesViewModel.localPhotosPermission.collect { permission ->
                    when {
                        permission.hasPermission -> {
                            deviceFilesViewModel.fetchLocalPhotos()

                            ctaButtonView.visibility = View.GONE
                        }
                        else -> {
                            deviceFilesViewModel.fetchSamplePhotos()

                            val ctaDescription = if (permission.hasNeverAsked) getString(R.string.request_permission_gallery_cta_description) else getString(R.string.request_permission_gallery_cta_description_denied_permission)
                            val ctaButtonText = if (permission.hasNeverAsked) getString(R.string.request_permission_gallery_cta_button) else getString(R.string.request_permission_gallery_cta_button_denied_permission)

                            ctaButtonView.apply {
                                visibility = View.VISIBLE
                                description = ctaDescription
                                buttonText = ctaButtonText
                                onClick = {
                                    if (permission.hasNeverAsked) deviceFilesViewModel.askPermissionLocalFiles()
                                    else deviceFilesViewModel.openAppSettings()
                                }
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                deviceFilesViewModel.devicePhotos.collect { photos ->
                    photoRecylerViewAdapter.submitList(photos)
                }
            }
        }
    }
}