package earth.levi.sage.android.fragment


import android.content.Intent
import android.content.Intent.EXTRA_STREAM
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.OnBackPressed
import earth.levi.sage.android.R
import earth.levi.sage.android.di.cloudFilesViewModel
import earth.levi.sage.android.di.devicePhotosViewModel
import earth.levi.sage.android.di.sharedActivityViewModelDiGraph
import earth.levi.sage.android.view.adapter.FolderRecyclerViewAdapter
import earth.levi.sage.android.widget.PhotoImageView
import earth.levi.sage.di.DiGraph
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.launch
import java.io.File

class PhotoFragment: Fragment(), OnBackPressed {

    private val devicePhotosViewModel by sharedActivityViewModelDiGraph {
        DiGraph.devicePhotosViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.photo_fragment)

            setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.action_share -> {
                        val photoToShare = devicePhotosViewModel.selectedPhoto ?: return@setOnMenuItemClickListener false

                        val localFilePathUri: Uri = when {
                            photoToShare.isLocal -> {
                                Uri.parse(File(photoToShare.localPhotoAsset!!.path).absolutePath)
                            }
                            photoToShare.isRemote -> {
                                // download photo to temp file on system then share.
                                Uri.parse(photoToShare.remotePhotoUrl!!)
                            }
                            else -> throw RuntimeException("should not happen")
                        }

                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(EXTRA_STREAM, localFilePathUri)

                            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(localFilePathUri.toString())
                            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())

                            type = mimeType
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                            try {
                                startActivity(Intent.createChooser(this, "Share photo via..."))
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }
                        }

                        true
                    }
                    else -> false
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                devicePhotosViewModel.observeSelectedPhoto.collect { it.let { photo ->
                    view.findViewById<PhotoImageView>(R.id.photo_imageview).apply {
                        shouldAnimateFromPlaceholder = false
                        this.photo = photo
                    }
                }}
            }
        }
    }

    override fun onBackPressed(): Boolean {
        devicePhotosViewModel.selectedPhoto = null

        return false
    }

}