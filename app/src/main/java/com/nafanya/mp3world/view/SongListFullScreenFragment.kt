package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentSongListFullScreenBinding
import com.nafanya.mp3world.model.SongListManager

class SongListFullScreenFragment : Fragment(R.layout.fragment_song_list_full_screen) {

    private lateinit var binding: FragmentSongListFullScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_song_list_full_screen, container, false)
        binding.songsRecycler.adapter = SongListAdapter(activity!!, SongListManager.getSongList(), preview = false)
        binding.songsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.songCount = SongListManager.getSongList().size
        return binding.root
    }
}
