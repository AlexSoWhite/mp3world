package com.nafanya.mp3world.features.metadataEditing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.BaseFragment
import com.nafanya.mp3world.databinding.FragmentEditMetadataLayoutBinding
import com.nafanya.mp3world.features.metadataEditing.MetadataEditActivity.Companion.SONG_URI_KEY
import javax.inject.Inject

class MetadataEditFragment :
    BaseFragment<FragmentEditMetadataLayoutBinding>() {

    @Inject
    lateinit var metadataEditViewModelFactory: MetadataEditViewModel.MetadataEditFactory.Factory
    private val viewModel: MetadataEditViewModel by viewModels {
        val uri = (arguments?.getString(SONG_URI_KEY, "") ?: "").toUri()
        metadataEditViewModelFactory.create(uri)
    }

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentEditMetadataLayoutBinding {
        return FragmentEditMetadataLayoutBinding.inflate(inflater, parent, attachToParent)
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .metadataEditComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            setDisplayHomeAsUpEnabled(true)
        }
        binding.songTitleInput.hint
        viewModel.song.observe(viewLifecycleOwner) {
            with(binding) {
                songIcon.setImageBitmap(it.art)
                songTitleInput.setText(it.title)
                songArtistInput.setText(it.artist)
                songAlbumInput.setText(it.album)
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    title = requireContext().getString(
                        R.string.song_description,
                        it.artist,
                        it.title
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
