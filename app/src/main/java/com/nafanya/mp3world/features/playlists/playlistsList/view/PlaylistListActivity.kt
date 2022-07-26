package com.nafanya.mp3world.features.playlists.playlistsList.view

import android.content.Intent
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
import com.nafanya.player.Playlist
import com.nafanya.mp3world.features.playlists.playlistsList.viewModel.PlaylistListViewModel
import javax.inject.Inject

class PlaylistListActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    override fun setViewModel() {
        viewModel = factory.create(PlaylistListViewModel::class.java)
    }

    override fun setAdapter() {
        val observer = Observer<MutableList<com.nafanya.player.Playlist>> {
            binding.recycler.adapter = PlaylistListAdapter(
                it
            ) { playlist, clickType ->
                when (clickType) {
                    ClickType.CLICK -> {
                        ActivityCreator()
                            .with(this)
                            .createMutablePlaylistActivity(
                                playlist,
                                viewModel as PlaylistListViewModel
                            ).start()
                    }
                    ClickType.LONG -> {
                        val intent = Intent(this, DeletePlaylistDialogActivity::class.java)
                        DeletePlaylistDialogActivity.prepare(
                            viewModel as PlaylistListViewModel,
                            playlist
                        )
                        startActivity(intent)
                    }
                }
            }
        }
        (viewModel as PlaylistListViewModel).playlists.observe(this, observer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.playlistListComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()
        binding.addPlaylist.addPlaylist.visibility = View.VISIBLE
        binding.addPlaylist.addPlaylist.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            AddPlaylistDialogActivity.setViewModel(viewModel as PlaylistListViewModel)
            startActivity(intent)
        }
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyPlaylistList.emptyPlaylistList.visibility = View.VISIBLE
        binding.emptyPlaylistList.emptyPlaylistList.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            AddPlaylistDialogActivity.setViewModel(viewModel as PlaylistListViewModel)
            startActivity(intent)
        }
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
                    (viewModel as PlaylistListViewModel).search(query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        (viewModel as PlaylistListViewModel).reset()
                    }
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }
}
