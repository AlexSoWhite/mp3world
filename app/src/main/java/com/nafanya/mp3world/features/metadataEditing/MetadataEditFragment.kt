package com.nafanya.mp3world.features.metadataEditing

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.stateMachines.common.StatedFragment
import com.nafanya.mp3world.core.stateMachines.common.StatedViewModel
import com.nafanya.mp3world.core.utils.showToast
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.databinding.FragmentEditMetadataLayoutBinding
import com.nafanya.mp3world.features.metadataEditing.MetadataEditActivity.Companion.SONG_URI_KEY
import javax.inject.Inject

@Suppress("TooManyFunctions")
class MetadataEditFragment :
    StatedFragment<FragmentEditMetadataLayoutBinding, LocalSong>() {

    companion object {
        const val permissionRequestCode = 0x1022
    }

    @Inject
    lateinit var metadataEditViewModelFactory: MetadataEditViewModel.MetadataEditFactory.Factory
    private val viewModel: MetadataEditViewModel by viewModels {
        val uri = (arguments?.getString(SONG_URI_KEY, "") ?: "").toUri()
        metadataEditViewModelFactory.create(uri)
    }

    override val statedViewModel: StatedViewModel<LocalSong>
        get() = viewModel

    private lateinit var currentSong: LocalSong
    private lateinit var pendingSong: LocalSong

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentEditMetadataLayoutBinding {
        return FragmentEditMetadataLayoutBinding.inflate(inflater, parent, attachToParent)
    }

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.metadataEditComponent.inject(this)
    }

    override fun renderInitializing() {
        // do nothing
    }

    override fun renderLoading() {
        updateViewsVisibility(isLoading = true)
    }

    override fun renderSuccess(data: LocalSong) {
        updateViewsVisibility(isLoading = false)
        renderContent(data)
    }

    override fun renderUpdated(data: LocalSong) {
        updateViewsVisibility(isLoading = false)
        renderContent(data)
    }

    override fun renderError(error: Error) = with(binding) {
        updateViewsVisibility(isLoading = true)
        binding.error.error.isVisible = true
        binding.error.textView.text = error.localizedMessage
        loader.loader.isVisible = false
    }

    private fun updateViewsVisibility(isLoading: Boolean) = with(binding) {
        songAlbumInputLayout.isVisible = !isLoading
        songArtistInputLayout.isVisible = !isLoading
        songTitleInputLayout.isVisible = !isLoading
        songIcon.isVisible = !isLoading
        submitButton.isVisible = !isLoading
        modifyIcon.isVisible = !isLoading
        modifyIconBackground.isVisible = !isLoading
        remove.isVisible = !isLoading
        error.error.isVisible = false
        loader.loader.isVisible = isLoading
    }

    private fun renderContent(song: LocalSong) = with(binding) {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            setDisplayHomeAsUpEnabled(true)
        }
        currentSong = song
        songIcon.setImageBitmap(song.art)
        songTitleInput.setText(song.title)
        songArtistInput.setText(song.artist)
        songAlbumInput.setText(song.album)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = requireContext().getString(
                R.string.song_description,
                song.artist,
                song.title
            )
        }
        submitButton.setOnClickListener {
            submit()
        }
        remove.setOnClickListener {
            viewModel.delete(song)
        }
    }

    private fun submit() = with(binding) {
        val title = songTitleInput.text.toString()
        val artist = songArtistInput.text.toString()
        val album = songAlbumInput.text.toString()
        if (title.isEmpty() || title.isBlank()) {
            requireContext().showToast("заполните название")
        } else if (artist.isEmpty() || artist.isBlank()) {
            requireContext().showToast("заполните исполнителя")
        } else if (album.isEmpty() || album.isBlank()) {
            requireContext().showToast("заполните альбом")
        } else {
            pendingSong = LocalSong(
                uri = currentSong.uri,
                art = currentSong.art,
                title = title,
                artist = artist,
                duration = currentSong.duration,
                date = currentSong.date,
                artistId = currentSong.artistId,
                albumId = currentSong.albumId,
                album = album,
                path = currentSong.path
            )
            viewModel.edit(pendingSong) {
                startIntentSenderForResult(
                    it,
                    permissionRequestCode,
                    null,
                    0,
                    0,
                    0,
                    null
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == permissionRequestCode) {
            viewModel.finishPendingEditing(pendingSong)
        }
    }
}
