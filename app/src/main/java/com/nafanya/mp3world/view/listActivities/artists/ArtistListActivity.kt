package com.nafanya.mp3world.view.listActivities.artists

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.view.ActivityCreator
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.artists.ArtistListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[ArtistListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<MutableList<Artist>> {
            binding.recycler.adapter = ArtistListAdapter(it) { artist ->
                ActivityCreator()
                    .with(this)
                    .createPlaylistActivity(artist.playlist!!)
                    .start()
            }
        }
        (viewModel as ArtistListViewModel).artistList.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyArtistList.emptyArtistList.visibility = View.VISIBLE
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
                    (viewModel as ArtistListViewModel).search(query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        (viewModel as ArtistListViewModel).reset()
                    }
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }
}
