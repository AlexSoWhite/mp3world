package com.nafanya.mp3world.view.listActivities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.RecyclerHolderActivityBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.OnSwipeListener
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

abstract class RecyclerHolderActivity : AppCompatActivity() {

    protected lateinit var binding: RecyclerHolderActivityBinding
    protected lateinit var viewModel: ListViewModelInterface
    private var playerView: StyledPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.recycler_holder_activity)

        // subscribing to viewModel
        setViewModel()
        subscribeToViewModel()
        setTitle()
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

        OnSwipeListener(binding.recycler) {
            this.finish()
        }

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
        binding.playerControlView.visibility = View.VISIBLE
        subscribeToPlayerState()
    }

    private fun subscribeToPlayerState() {
        // setting view
        playerView = findViewById(R.id.player_control_view)
        playerView?.showTimeoutMs = 0
        playerView?.isNestedScrollingEnabled = false
        // observe current song state
        val observerSong = Observer<Song> {
            findViewById<TextView>(R.id.track_title).text = it.title
            findViewById<TextView>(R.id.track_artist).text = it.artist
        }
        ForegroundServiceLiveDataProvider.currentSong.observe(this, observerSong)
        // observe player state
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView?.player = ForegroundServiceLiveDataProvider.getPlayer()
                playerView?.repeatToggleModes =
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
                    RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
            }
        }
        ForegroundServiceLiveDataProvider.isPlayerInitialized.observe(
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
