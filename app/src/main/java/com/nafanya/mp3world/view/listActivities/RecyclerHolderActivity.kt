package com.nafanya.mp3world.view.listActivities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.RecyclerHolderActivityBinding
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.view.playerViews.GenericPlayerControlView
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import com.r0adkll.slidr.Slidr

@Suppress("TooManyFunctions")
abstract class RecyclerHolderActivity : AppCompatActivity() {

    protected lateinit var binding: RecyclerHolderActivityBinding
    protected lateinit var viewModel: ListViewModelInterface
    private var playerView: GenericPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.recycler_holder_activity)

        Slidr.attach(this)
        // subscribing to viewModel
        setViewModel()
        subscribeToViewModel()
        setTitle()
    }

    override fun onStart() {
        super.onStart()
        Log.d("Slide", this.localClassName)
    }

    // default for SongListActivity
    protected open fun setTitle() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val observer = Observer<String> {
            supportActionBar?.title = it
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewModel.title.observe(this, observer)
    }

    private fun subscribeToViewModel() {
        val observer = Observer<PageState> {
            when (it) {

                PageState.IS_LOADING -> onLoading()

                PageState.IS_LOADED -> onLoaded()

                PageState.IS_EMPTY -> onEmpty()

                null -> {
                    // TODO display error
                }
            }
        }
        viewModel.pageState.observe(this, observer)
    }

    open fun onLoading() {
        viewModel.onLoading()
        binding.recyclerWrapper.visibility = View.INVISIBLE
        binding.playerControlView.visibility = View.INVISIBLE
        binding.loader.loader.visibility = View.VISIBLE
    }

    open fun onLoaded() {
        viewModel.onLoaded()
        binding.recyclerWrapper.visibility = View.VISIBLE
        binding.playerControlView.visibility = View.VISIBLE
        binding.loader.loader.visibility = View.INVISIBLE

        binding.emptyPlaylist.emptyPlaylist.visibility = View.INVISIBLE
        binding.emptyPlaylistList.emptyPlaylistList.visibility = View.INVISIBLE
        binding.emptySongList.emptySongList.visibility = View.INVISIBLE
        binding.emptyAlbumList.emptyAlbumList.visibility = View.INVISIBLE
        binding.emptyArtistList.emptyArtistList.visibility = View.INVISIBLE
        binding.emptySearchResult.emptySearchResult.visibility = View.INVISIBLE

        // subscribing to playerState
        subscribeToPlayerState()

        // list setting
        setAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        // custom options
        addCustomBehavior()

        // animate elements arrive in list
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = duration
        alphaAnimation.startOffset = startOffset
        binding.recycler.startAnimation(alphaAnimation)
    }

    open fun onEmpty() {
        viewModel.onEmpty()
        binding.loader.loader.visibility = View.INVISIBLE
        binding.addSongToPlaylist.addSongToPlaylist.visibility = View.GONE
        binding.addPlaylist.addPlaylist.visibility = View.GONE
        binding.playerControlView.visibility = View.VISIBLE
        subscribeToPlayerState()
    }

    private fun subscribeToPlayerState() {
        // observe player state
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = GenericPlayerControlView(this, R.id.player_control_view)
                playerView!!.setSongObserver()
                findViewById<LinearLayout>(R.id.controls_view).setOnClickListener {
                    playerView!!.toFullScreen()
                }
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return false
    }

    open fun addCustomBehavior() {}

    // requires child activity to set viewModel
    abstract fun setViewModel()

    // requires child activity to set binding.recycler.adapter
    abstract fun setAdapter()

    companion object {
        private const val duration = 500L
        private const val startOffset = 350L
    }
}
