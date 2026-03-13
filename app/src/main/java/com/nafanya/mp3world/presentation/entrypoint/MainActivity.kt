package com.nafanya.mp3world.presentation.entrypoint

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.presentation.core.navigation.ActivityStarter
import com.nafanya.mp3world.presentation.core.common_ui.BaseActivity
import com.nafanya.mp3world.databinding.ActivityMainLayoutBinding
import com.nafanya.mp3world.presentation.foreground_service.ForegroundService
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainLayoutBinding>() {

    private companion object {
        const val TAG = "_MainActivity"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: InitialViewModel by viewModels { factory }

    override fun inflate(layoutInflater: LayoutInflater): ActivityMainLayoutBinding {
        return ActivityMainLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PlayerApplication).applicationComponent
            .entrypointComponent
            .inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        checkPermissionsAndInitializeLists()
    }

    // part of onCreate
    // todo: request rationale
    private fun checkPermissionsAndInitializeLists() {
        val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
        if (checkSelfPermission(permissionRead) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permissionRead), 0) // triggers onPermissionResult
            return
        }
        val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P
        ) {
            if (checkSelfPermission(permissionWrite) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionWrite), 0)
                return
            }
        }
        initMainMenu()
    }

    override fun onResume() {
        super.onResume()
        startServiceIfNeeded()
    }

    private fun startServiceIfNeeded() {
        Log.d(TAG, "startServiceIfNeeded")
        val activityManager =
            this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
        val isServiceRunning = runningServices
            ?.find { it.service.className == ForegroundService::class.java.name }
            ?.foreground == true
        if (!isServiceRunning) {
            Log.d(TAG, "startServiceIfNeeded - service is not in foreground, restarting")
            val intent =
                Intent(this.applicationContext, ForegroundService::class.java)
            ContextCompat.startForegroundService(this.applicationContext, intent)
        }
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
        checkPermissionsAndInitializeLists()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    // todo(New API)
    override fun onBackPressed() {
        finishAfterTransition()
        super.onBackPressed()
    }
}
