package earth.levi.sage.android.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.databinding.AdapterFolderRecyclerviewBinding
import earth.levi.sage.db.Folder

class FolderRecyclerViewAdapter : ListAdapter<Folder, FolderRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Folder>() {
            override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean = oldItem.name == newItem.name
            override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean = oldItem == newItem
        }
    }

    class ViewHolder(private val binding: AdapterFolderRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Folder) {
            binding.albumNameTextview.text = album.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AdapterFolderRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
