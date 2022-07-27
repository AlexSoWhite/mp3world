package com.nafanya.mp3world.features.searching.view

import android.os.Bundle
import android.view.View
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.RecyclerHolderActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.features.allSongs.SongListAdapter
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.searching.viewModel.SearchSongListViewModel
import javax.inject.Inject

class SearchSongListActivity : RecyclerHolderActivity() {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var downloadViewModel: DownloadViewModel

    override fun setViewModel() {
        viewModel = factory.create(SearchSongListViewModel::class.java)
        downloadViewModel = factory.create(DownloadViewModel::class.java)
    }

    override fun setAdapter() {
        (viewModel as SearchSongListViewModel).playlist.observe(this) {
            binding.recycler.adapter = SongListAdapter(
                it,
                null
            ) { playlist, song ->
                viewModel.onClick(playlist, song)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.searchSongsComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptySearchResult.emptySearchResult.visibility = View.VISIBLE
    }
}
