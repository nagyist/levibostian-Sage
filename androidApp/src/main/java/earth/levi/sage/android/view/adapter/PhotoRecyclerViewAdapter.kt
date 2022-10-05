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

    lateinit var onItemClick: (Int, Photo) -> Unit

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
            binding.photoImageview.photo = photo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPhotoRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding).also { viewHolder ->
            // set click listener in onCreate function instead of onBind for better performance.
            binding.photoImageview.setOnClickListener {
                onItemClick(viewHolder.bindingAdapterPosition, getItem(viewHolder.bindingAdapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
