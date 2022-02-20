package com.nafanya.mp3world.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundService
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.artists.ArtistListActivity
import com.nafanya.mp3world.view.listActivities.playlists.PlaylistListActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.MainActivityViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var playerView: StyledPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        checkPermissions()
    }

    // part of onCreate
    private fun checkPermissions() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 0) // triggers onPermissionResult
            } else {
                mainActivityViewModel.initializeLists(this)
                initMainMenu()
                subscribeToPlayerState()
                initService()
            }
        }
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

    private fun initService() {
        // init service
        val intent = Intent(applicationContext, ForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.songCount = SongListManager.songList.size
        binding.artistCount = ArtistListManager.artists.size
        binding.albumCount = 0
        binding.favoriteCount = 0
    }

    private fun initMainMenu() {
        // all songs
        binding.allSongs.item.setOnClickListener {
            val songListIntent = Intent(this, SongListActivity::class.java)
            SongListViewModel.newInstanceWithPlaylist(
                Playlist(
                    SongListManager.songList,
                    id = 0,
                    name = "Мои песни"
                )
            )
            startActivity(songListIntent)
        }

        // playlists
        val observer = Observer<List<Playlist>> {
            binding.playlistCount = it.size
        }
        PlaylistListManager.playlists.observe(this, observer)
        binding.playlists.item.setOnClickListener {
            val playlistIntent = Intent(this, PlaylistListActivity::class.java)
            startActivity(playlistIntent)
        }

        // artists
        binding.artists.item.setOnClickListener {
            val artistsIntent = Intent(this, ArtistListActivity::class.java)
            startActivity(artistsIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // setting appBar search view
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val intent = Intent(this@MainActivity, SongListActivity::class.java)
                    SongListViewModel.newInstanceWithQuery(query)
                    startActivity(intent)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.stopService(Intent(this, ForegroundService::class.java))
    }
}
