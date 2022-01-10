package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentSongListFullScreenBinding
import com.nafanya.mp3world.model.OnSwipeTouchListener
import com.nafanya.mp3world.model.SongListManager

class SongListFullScreenFragment : Fragment(R.layout.fragment_song_list_full_screen) {

    private lateinit var binding: FragmentSongListFullScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_song_list_full_screen,
            container,
            false
        )
        binding.songsRecycler.adapter = SongListAdapter(
            SongListManager.getSongList()
        )
        binding.songsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.songCount = SongListManager.getSongList().size
        OnSwipeTouchListener(activity!!, binding.songsList) {
            closeFragment()
        }
        OnSwipeTouchListener(activity!!, activity?.findViewById(R.id.songs_list_container)!!) {
            closeFragment()
        }
        OnSwipeTouchListener(activity!!, binding.songsRecycler) {
            closeFragment()
        }
        return binding.root
    }

    private fun closeFragment() {
        val fragment = SongListPreviewFragment()
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        parentFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addSharedElement(binding.songsList, "song_list")
            replace(R.id.songs_list_container, fragment)
            commit()
        }
    }
}
