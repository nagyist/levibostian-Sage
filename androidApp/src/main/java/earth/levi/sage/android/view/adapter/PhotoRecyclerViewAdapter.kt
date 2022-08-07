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
import com.squareup.picasso.Picasso
import earth.levi.sage.android.databinding.AdapterFolderRecyclerviewBinding
import earth.levi.sage.android.databinding.AdapterPhotoRecyclerviewBinding
import earth.levi.sage.db.Folder
import earth.levi.sage.type.*
import java.io.File

class PhotoRecyclerViewAdapter : ListAdapter<Photo, PhotoRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
        }
    }

    class ViewHolder(private val binding: AdapterPhotoRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Photo) {
            Picasso.get().load(Uri.fromFile(File(album.path)))
                .resize(200, 200)
                .centerCrop()
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
