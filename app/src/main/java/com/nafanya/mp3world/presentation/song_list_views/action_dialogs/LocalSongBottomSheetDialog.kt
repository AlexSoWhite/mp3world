package com.nafanya.mp3world.presentation.song_list_views.action_dialogs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.wrappers.song.ArtistMetadata
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.all_songs.SongPlaylistProvider
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import javax.inject.Inject
import kotlinx.coroutines.launch

sealed interface LocalSongAction {
    data object AddToFavorite : LocalSongAction
    data object RemoveFromFavorite : LocalSongAction
    data object GoToAlbum : LocalSongAction
    data class GoToArtist(val artist: ArtistMetadata) : LocalSongAction
}

class LocalSongBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var allSongsListProvider: SongPlaylistProvider

    @Inject
    lateinit var favouritesManager: FavouritesProvider

    companion object {

        private const val URI_KEY = "URI_KEY"

        fun newInstance(song: LocalSong): LocalSongBottomSheetDialog {
            val bundle = bundleOf(URI_KEY to song.uri)
            return LocalSongBottomSheetDialog().apply { arguments = bundle }
        }
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as PlayerApplication).applicationComponent
            .allSongsComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = requireArguments()
        val uri = args.getParcelable<Uri>(URI_KEY)
        return ComposeView(inflater.context).apply {
            setContent {
                val songList by allSongsListProvider.songList.collectAsState(emptyList())
                val song = songList.find { it.uri == uri }
                val coroutineScope = rememberCoroutineScope()
                song?.let {
                    val isFavourite by favouritesManager.observeIsSongInFavorites(song).collectAsState(false)

                    LocalSongActionDialogView(
                        songTitle = song.title,
                        songArtists = song.artists,
                        songAlbum = song.album,
                        isFavorite = isFavourite,
                        onAction = {
                            when (it) {
                                LocalSongAction.AddToFavorite -> {
                                    coroutineScope.launch { favouritesManager.add(song) }
                                }
                                LocalSongAction.RemoveFromFavorite -> {
                                    coroutineScope.launch { favouritesManager.delete(song) }
                                }
                                LocalSongAction.GoToAlbum -> requireActivity().navigateToAlbum(song)
                                is LocalSongAction.GoToArtist -> requireActivity().navigateToArtist(it.artist)
                            }
                        }
                    )
                }
            }
        }
    }
}
