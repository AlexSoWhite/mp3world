package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentContainerBinding

class FragmentContainer : Fragment(R.layout.fragment_container) {

    private lateinit var binding: FragmentContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_container, container, false)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.songs_list_container, SongListPreviewFragment())
            replace(R.id.playlists_list_container, PlaylistListPreviewFragment())
            commit()
        }
        return binding.root
    }
}
