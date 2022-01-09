package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ContainerBinding
import com.nafanya.mp3world.databinding.FragmentSongListFullScreenBinding

class FragmentContainer : Fragment(R.layout.container) {

    private lateinit var binding: ContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.container, container, false)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.songs_list_container, SongListPreviewFragment())
            replace(R.id.playlists_list_container, PlaylistListPreviewFragment())
            commit()
        }
        return binding.root
    }
}