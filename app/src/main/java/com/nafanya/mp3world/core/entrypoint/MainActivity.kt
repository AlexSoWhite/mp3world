package com.nafanya.mp3world.core.entrypoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.ActivityCreator
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.features.foregroundService.ForegroundService
import com.nafanya.mp3world.features.foregroundService.ServiceInitializer
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: InitialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.entrypointComponent().inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
        val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            if (checkSelfPermission(permissionWrite) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionWrite), 0)
                return
            }
        }
        viewModel = factory.create(InitialViewModel::class.java)
        viewModel.initializeLists()
        PRDownloader.initialize(applicationContext)
        initInitializer()
        initMainMenu()
    }

    private fun initInitializer() {
        val intent = Intent(applicationContext, ServiceInitializer::class.java)
        startService(intent)
    }

    @Suppress("LongMethod")
    private fun initMainMenu() {
        // all songs
        binding.allSongs.menuItemIcon.setImageResource(R.drawable.song_menu_icon)
        viewModel.songList.observeForever { songList ->
            binding.root.post {
                binding.songCount = songList.size
                binding.allSongs.item.setOnClickListener {
                    ActivityCreator()
                        .with(this)
                        .createSongListActivity(songList)
                        .start()
                }
            }
        }

        // playlists
        binding.playlists.menuItemIcon.setImageResource(R.drawable.playlist_play)
        viewModel.playlists.observeForever { playlists ->
            binding.root.post {
                binding.playlistCount = playlists.size
                binding.playlists.item.setOnClickListener {
                    ActivityCreator()
                        .with(this)
                        .createPlaylistListActivity()
                        .start()
                }
            }
        }

        // artists
        binding.artists.menuItemIcon.setImageResource(R.drawable.artist)
        viewModel.artists.observeForever { artists ->
            binding.root.post {
                binding.artistCount = artists.size
                binding.artists.item.setOnClickListener {
                    ActivityCreator()
                        .with(this)
                        .createArtistListActivity()
                        .start()
                }
            }
        }

        // albums
        binding.albums.menuItemIcon.setImageResource(R.drawable.album)
        viewModel.albums.observeForever { albums ->
            binding.root.post {
                binding.albumCount = albums.size
                binding.albums.item.setOnClickListener {
                    ActivityCreator()
                        .with(this)
                        .createAlbumListActivity()
                        .start()
                }
            }
        }

        // favourites
        binding.favourite.menuItemIcon.setImageResource(R.drawable.favorite)
        viewModel.favourites.observeForever { playlist ->
            binding.root.post {
                binding.favouriteCount = playlist.songList.size
                binding.favourite.item.setOnClickListener {
                    ActivityCreator()
                        .with(this)
                        .createFavouriteListActivity(playlist)
                        .start()
                }
            }
        }

        // statistic
//        val statisticObserver = Observer<MutableList<SongStatisticEntity>> {
//            binding.statistics.item.setOnClickListener {
//                val statisticIntent = Intent(this, StatisticActivity::class.java)
//                startActivity(statisticIntent)
//            }
//        }
//        StatisticInfoManager.info.observe(this, statisticObserver)
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
                    ActivityCreator()
                        .with(this@MainActivity)
                        .createSearchSongListActivity(query)
                        .start()
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.stopService(Intent(this, ForegroundService::class.java))
    }
}
