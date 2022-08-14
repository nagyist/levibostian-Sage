package earth.levi.sage.android.view.adapter


import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import earth.levi.sage.android.R
import earth.levi.sage.android.databinding.AdapterFolderRecyclerviewBinding
import earth.levi.sage.android.databinding.AdapterPhotoRecyclerviewBinding
import earth.levi.sage.db.Folder
import earth.levi.sage.type.*
import java.io.File
import java.lang.Exception

class PhotoRecyclerViewAdapter : ListAdapter<Photo, PhotoRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                oldItem.localPhotoAsset?.let {
                    return it.id == newItem.localPhotoAsset?.id
                }
                oldItem.remotePhotoUrl?.let {
                    return it == newItem.remotePhotoUrl
                }

                return false
            }
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = areContentsTheSame(oldItem, newItem)
        }
    }

    class ViewHolder(private val binding: AdapterPhotoRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            val picassoInstance = Picasso.get()

            val requestBuilder = when {
                photo.isLocal -> picassoInstance.load(Uri.fromFile(File(photo.localPhotoAsset!!.path)))
                photo.isRemote -> picassoInstance.load(photo.remotePhotoUrl!!)
                else -> throw RuntimeException("should not happen")
            }

            requestBuilder
                .placeholder(R.drawable.ic_baseline_cloud_24)
                .resize(400, 400)
                .centerCrop()
                .error(R.drawable.ic_baseline_smartphone_24)
                .into(binding.photoImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AdapterPhotoRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
