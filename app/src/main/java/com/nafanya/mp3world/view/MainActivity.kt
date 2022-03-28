package com.nafanya.mp3world.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.downloader.PRDownloader
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundService
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.foregroundService.ServiceInitializer
import com.nafanya.mp3world.model.listManagers.AlbumListManager
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.listManagers.StatisticInfoManager
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity
import com.nafanya.mp3world.view.listActivities.albums.AlbumListActivity
import com.nafanya.mp3world.view.listActivities.artists.ArtistListActivity
import com.nafanya.mp3world.view.listActivities.favourite.FavouriteListActivity
import com.nafanya.mp3world.view.listActivities.playlists.PlaylistListActivity
import com.nafanya.mp3world.view.listActivities.search.SearchSongListActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.view.listActivities.statistic.StatisticActivity
import com.nafanya.mp3world.view.playerViews.FullScreenPlayerActivity
import com.nafanya.mp3world.view.playerViews.GenericPlayerControlView
import com.nafanya.mp3world.viewmodel.MainActivityViewModel
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var playerView: GenericPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        checkPermissions()
    }

    // part of onCreate
    private fun checkPermissions() {
        val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissionRead) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionRead), 0) // triggers onPermissionResult
                return
            }
        }
        mainActivityViewModel.initializeLists()
        initMainMenu()
        initInitializer()
        PRDownloader.initialize(applicationContext)
        subscribeToPlayerState()
    }

    private fun initInitializer() {
        val intent = Intent(applicationContext, ServiceInitializer::class.java)
        startService(intent)
    }

    private fun subscribeToPlayerState() {
        // observe player state
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = GenericPlayerControlView(this, R.id.player_control_view)
                playerView!!.setSongObserver()
                playerView!!.playerControlView.visibility = View.VISIBLE
                findViewById<ShapeableImageView>(R.id.fullscreen).setOnClickListener {
                    val intent = Intent(this, FullScreenPlayerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
    }

    @Suppress("LongMethod")
    private fun initMainMenu() {
        // all songs
        val songListObserver = Observer<MutableList<Song>> { songList ->
            binding.songCount = songList.size
            binding.allSongs.item.setOnClickListener {
                val songListIntent = Intent(this, SongListActivity::class.java)
                SourceProvider.newInstanceWithPlaylist(
                    Playlist(
                        songList,
                        id = -1,
                        name = "Мои песни"
                    )
                )
                startActivity(songListIntent)
            }
        }
        SongListManager.songList.observe(this, songListObserver)

        // playlists
        val playlistsObserver = Observer<MutableList<Playlist>> {
            binding.playlistCount = it.size
            binding.playlists.item.setOnClickListener {
                val playlistIntent = Intent(this, PlaylistListActivity::class.java)
                startActivity(playlistIntent)
            }
        }
        PlaylistListManager.playlists.observe(this, playlistsObserver)

        // artists
        val artistObserver = Observer<MutableList<Artist>> {
            binding.artistCount = it.size
            binding.artists.item.setOnClickListener {
                val artistsIntent = Intent(this, ArtistListActivity::class.java)
                startActivity(artistsIntent)
            }
        }
        ArtistListManager.artists.observe(this, artistObserver)

        // albums
        val albumsObserver = Observer<MutableList<Album>> {
            binding.albumCount = it.size
            binding.albums.item.setOnClickListener {
                val albumsIntent = Intent(this, AlbumListActivity::class.java)
                startActivity(albumsIntent)
            }
        }
        AlbumListManager.albums.observe(this, albumsObserver)

        // favourites
        val favouriteObserver = Observer<MutableList<Song>> { songList ->
            binding.favouriteCount = songList.size
            binding.favourite.item.setOnClickListener {
                val favouriteIntent = Intent(this, FavouriteListActivity::class.java)
                SourceProvider.newInstanceWithPlaylist(
                    Playlist(
                        songList,
                        id = -1,
                        name = "Избранное"
                    )
                )
                startActivity(favouriteIntent)
            }
        }
        FavouriteListManager.favorites.observe(this, favouriteObserver)

        // statistic
        val statisticObserver = Observer<MutableList<SongStatisticEntity>> {
            binding.statistics.item.setOnClickListener {
                val statisticIntent = Intent(this, StatisticActivity::class.java)
                startActivity(statisticIntent)
            }
        }
        StatisticInfoManager.info.observe(this, statisticObserver)
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
                    val intent = Intent(this@MainActivity, SearchSongListActivity::class.java)
                    SourceProvider.newInstanceWithQuery(query)
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
