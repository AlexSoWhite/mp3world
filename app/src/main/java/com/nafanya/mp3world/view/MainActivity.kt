package com.nafanya.mp3world.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.SongListManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        SongListManager.initializeSongList(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, FragmentContainer())
            commit()
        }
//        binding.songsRecycler.adapter = SongListAdapter(this, SongListManager.getSongList())
//        binding.songsRecycler.layoutManager = LinearLayoutManager(this)
//        binding.songCount = SongListManager.getSongList().size
    }
}
