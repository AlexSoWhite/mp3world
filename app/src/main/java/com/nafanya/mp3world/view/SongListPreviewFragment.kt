package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentSongListPreviewBinding
import com.nafanya.mp3world.model.SongListManager

class SongListPreviewFragment : Fragment(R.layout.fragment_song_list_preview) {

    private lateinit var binding: FragmentSongListPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_song_list_preview,
            container,
            false
        )
        binding.songsRecycler.adapter = SongListAdapter(
            SongListManager.getSongList()
        )
        binding.songsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.songCount = SongListManager.getSongList().size
        // exitTransition = Hold()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set(parentFragmentManager, binding)
        binding.songsRecycler.addOnScrollListener(ScrollListener)
    }

    companion object ScrollListener : RecyclerView.OnScrollListener() {

        private lateinit var parentFragmentManager: FragmentManager
        private lateinit var binding: FragmentSongListPreviewBinding
        fun set(parentFragmentManager: FragmentManager, binding: FragmentSongListPreviewBinding) {
            this.parentFragmentManager = parentFragmentManager
            this.binding = binding
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val fragment = SongListFullScreenFragment()
                fragment.sharedElementEnterTransition = MaterialContainerTransform()
                parentFragmentManager.beginTransaction().apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    addSharedElement(binding.songsList, "song_list")
                    replace(R.id.songs_list_container, fragment)
                    commit()
                }
            }
        }
    }
}
