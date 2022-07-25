package earth.levi.sage.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import earth.levi.sage.android.di.filesViewModel
import earth.levi.sage.android.di.viewModelDiGraph
import earth.levi.sage.android.view.adapter.FolderRecyclerViewAdapter
import earth.levi.sage.di.DiGraph
import earth.levi.sage.kotlin_inline.fold
import earth.levi.sage.type.result.GetFolderContentsResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val folderRecyclerViewAdapter = FolderRecyclerViewAdapter()

    private val filesViewModel by viewModelDiGraph {
        DiGraph.filesViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.albums_recyclerview).apply {
            adapter = folderRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
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
