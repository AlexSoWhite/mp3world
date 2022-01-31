package com.nafanya.mp3world.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.Downloader
import com.nafanya.mp3world.model.ForegroundService
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerView: StyledPlayerControlView
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = ""
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playerView = findViewById(R.id.player_control_view)
        playerView.showTimeoutMs = 0
        playerView.isNestedScrollingEnabled = false
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        checkPermissions()
    }

    private fun checkPermissions() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 0)
            } else {
                mainActivityViewModel.initialize(this)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container, FragmentContainer())
                    commit()
                }
                val observerSong = Observer<Song> {
                    findViewById<TextView>(R.id.track_title).text = it.title
                    findViewById<TextView>(R.id.track_artist).text = it.artist
                }
                ForegroundServiceLiveDataProvider.currentSong.observe(this, observerSong)
                val observerPlayer = Observer<Boolean> {
                    if (it) {
                        playerView.player = ForegroundServiceLiveDataProvider.getPlayer()
                        playerView.repeatToggleModes =
                            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
                            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
                            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
                    }
                }
                ForegroundServiceLiveDataProvider.isPlayerInitialized.observe(this, observerPlayer)
                val intent = Intent(applicationContext, ForegroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                }
            }
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
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Downloader.preLoad(query) { playlist ->
                        playlist?.let {
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.container, SearchFragment.newInstance(it))
                                commit()
                            }
                            return@let
                        }
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty() || newText.isBlank()) {
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.container, FragmentContainer())
                            commit()
                        }
                    }
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
