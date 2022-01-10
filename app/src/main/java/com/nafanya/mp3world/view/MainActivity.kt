package com.nafanya.mp3world.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.Listener
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.model.SongListManager
import com.nafanya.mp3world.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerView: StyledPlayerControlView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playerView = findViewById(R.id.player_control_view)
        playerView.showTimeoutMs = 0
        checkPermissions()
    }

    private fun checkPermissions() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 0)
            } else {
                val mainActivityViewModel =
                    ViewModelProvider(this)[MainActivityViewModel::class.java]
                Listener.setViewModel(mainActivityViewModel)
                mainActivityViewModel.initialize(this, playerView)
                val songObserver = Observer<Song> {
                    playerView.findViewById<TextView>(R.id.track_title).text = it.title
                    playerView.findViewById<TextView>(R.id.track_artist).text = it.artist
                }
                mainActivityViewModel.currentSong.observe(this, songObserver)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container, FragmentContainer())
                    commit()
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
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SongListManager.initializeSongList(this)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, FragmentContainer())
                commit()
            }
        }
    }
}
