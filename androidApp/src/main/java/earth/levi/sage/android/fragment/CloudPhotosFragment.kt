package earth.levi.sage.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.R
import earth.levi.sage.android.di.filesViewModel
import earth.levi.sage.android.di.sharedActivityViewModelDiGraph
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.view.adapter.FolderRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.launch

class CloudPhotosFragment: Fragment() {

    private val folderRecyclerViewAdapter = FolderRecyclerViewAdapter()

    private val filesViewModel by sharedActivityViewModelDiGraph {
        DiGraph.filesViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cloud_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recyclerview).apply {
            adapter = folderRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
            // hard-coded photo path for now. that way we are guaranteed to find only photos instead of having to browse folders, too.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filesViewModel.albumsForPath("/Photos").collect { folders ->
                    folderRecyclerViewAdapter.submitList(folders)
                }
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
                            filesViewModel.loginToHostingService(requireContext())
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