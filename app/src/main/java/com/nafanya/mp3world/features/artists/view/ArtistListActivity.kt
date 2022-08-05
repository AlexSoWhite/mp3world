package com.nafanya.mp3world.features.artists.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.ActivityCreator
import com.nafanya.mp3world.core.view.RecyclerHolderActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.viewModel.ArtistListViewModel
import javax.inject.Inject

class ArtistListActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    override fun setViewModel() {
        viewModel = factory.create(ArtistListViewModel::class.java)
    }

    override fun setAdapter() {
        (viewModel as ArtistListViewModel).artistList.observe(this) {
            binding.recycler.adapter = ArtistListAdapter(it) { artist ->
                ActivityCreator()
                    .with(this)
                    .createPlaylistActivity(artist.playlist!!)
                    .start()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.artistsComponent().inject(this)
        super.onCreate(savedInstanceState)
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
