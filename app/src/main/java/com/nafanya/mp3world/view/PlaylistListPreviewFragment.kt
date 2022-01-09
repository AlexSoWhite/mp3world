package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentPlaylistListPreviewBinding
import com.nafanya.mp3world.model.SongListManager

class PlaylistListPreviewFragment : Fragment(R.layout.fragment_playlist_list_preview) {

    private lateinit var binding: FragmentPlaylistListPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_playlist_list_preview,
            container,
            false
        )
        binding.playlistCount = 10
        binding.playlistsRecycler.adapter = SongListAdapter(
            activity!!,
            SongListManager.getSongList()
        )
        binding.playlistsRecycler.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }
}
