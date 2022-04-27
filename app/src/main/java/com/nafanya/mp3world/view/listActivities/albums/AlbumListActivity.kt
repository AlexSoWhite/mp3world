package com.nafanya.mp3world.view.listActivities.albums

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.view.ActivityCreator
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.albums.AlbumListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[AlbumListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<MutableList<Album>> {
            binding.recycler.adapter = AlbumListAdapter(it) { album ->
                ActivityCreator()
                    .with(this)
                    .createPlaylistActivity(album.playlist!!)
                    .start()
            }
        }
        (viewModel as AlbumListViewModel).albumsList.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyAlbumList.emptyAlbumList.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // setting appBar search view
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    (viewModel as AlbumListViewModel).search(query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        (viewModel as AlbumListViewModel).reset()
                    }
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }
}
