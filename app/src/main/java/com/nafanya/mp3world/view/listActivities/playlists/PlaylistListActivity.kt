package com.nafanya.mp3world.view.listActivities.playlists

import android.content.Intent
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.ActivityCreator
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistListActivity : RecyclerHolderActivity() {

    override fun setAdapter() {
        val observer = Observer<MutableList<Playlist>> {
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

    override fun addCustomBehavior() {
        super.addCustomBehavior()
        binding.addPlaylist.addPlaylist.visibility = View.VISIBLE
        binding.addPlaylist.addPlaylist.setOnClickListener {
            val intent = Intent(this, AddPlaylistDialogActivity::class.java)
            AddPlaylistDialogActivity.setViewModel(viewModel as PlaylistListViewModel)
            startActivity(intent)
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[PlaylistListViewModel::class.java]
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
