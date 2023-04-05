package com.nafanya.mp3world.core.entrypoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.ActivityMainLayoutBinding
import com.nafanya.mp3world.features.foregroundService.ForegroundService
import com.nafanya.mp3world.features.foregroundService.ServiceMonitor
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainLayoutBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    val viewModel: InitialViewModel by viewModels { factory }

    /**
     * Service initializer.
     */
    @Inject
    lateinit var serviceMonitor: ServiceMonitor

    override fun inflate(layoutInflater: LayoutInflater): ActivityMainLayoutBinding {
        return ActivityMainLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent
            .entrypointComponent
            .inject(this)
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
        viewModel.initializeLists()
        serviceMonitor.startMonitoring()
        initMainMenu()
    }

    @Suppress("LongMethod")
    private fun initMainMenu() = binding.apply {
        allSongs.bindDataSource(viewModel.songModel, lifecycleScope)
        playlists.bindDataSource(viewModel.playlists, lifecycleScope)
        artists.bindDataSource(viewModel.artists, lifecycleScope)
        albums.bindDataSource(viewModel.albums, lifecycleScope)
        favourites.bindDataSource(viewModel.favourites, lifecycleScope)
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
        applicationContext.stopService(Intent(this, ForegroundService::class.java))
        viewModel.suspendPlayer()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finishAfterTransition()
        super.onBackPressed()
    }
}
