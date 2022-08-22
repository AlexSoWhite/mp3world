package com.nafanya.mp3world.core.entrypoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.ActivityMainLayoutBinding
import com.nafanya.mp3world.features.foregroundService.ForegroundService
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainLayoutBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: InitialViewModel

    override fun inflate(layoutInflater: LayoutInflater): ActivityMainLayoutBinding {
        return ActivityMainLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent.entrypointComponent().inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
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
        observeInitialization()
        initMainMenu()
    }

    // TODO consider systematizing
    private fun observeInitialization() {
        viewModel.initializationLiveData.observe(this) {
            if (it) {
                val intent = Intent(this.applicationContext, ForegroundService::class.java)
                ContextCompat.startForegroundService(this.applicationContext, intent)
            }
        }
    }

    @Suppress("LongMethod")
    private fun initMainMenu() = binding.apply {
        val context = this@MainActivity
        // all songs
        allSongs.setOnClickListener {
            ActivityStarter.Builder()
                .with(context)
                .createIntentToAllSongsActivity()
                .build()
                .startActivity()
        }
        viewModel.songList.observe(context) { songList ->
            allSongs.count.text = songList.size.toString()
        }

        // playlists
        playlists.setOnClickListener {
            ActivityStarter.Builder()
                .with(context)
                .createIntentToAllPlaylistsActivity()
                .build()
                .startActivity()
        }
        viewModel.playlists.observe(context) { allPlaylists ->
            playlists.count.text = allPlaylists.size.toString()
        }

        // artists
        artists.setOnClickListener {
            ActivityStarter.Builder()
                .with(context)
                .createIntentToArtistListActivity()
                .build()
                .startActivity()
        }
        viewModel.artists.observeForever { artistsList ->
            artists.count.text = artistsList.size.toString()
        }

        // albums
        albums.setOnClickListener {
            ActivityStarter.Builder()
                .with(context)
                .createIntentToAlbumListActivity()
                .build()
                .startActivity()
        }
        viewModel.albums.observe(context) { albumsList ->
            albums.count.text = albumsList.size.toString()
        }

        // favourites
        favourite.setOnClickListener {
            ActivityStarter.Builder()
                .with(context)
                .createIntentToFavouritesActivity()
                .build()
                .startActivity()
        }
        viewModel.favourites.observeForever { playlist ->
            favourite.count.text = playlist?.songList?.size.toString()
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
                    ActivityStarter.Builder()
                        .with(this@MainActivity)
                        .createIntentToRemoteSongsActivity(query)
                        .build()
                        .startActivity()
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
