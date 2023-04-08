package com.nafanya.mp3world.features.metadataEditing

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.allSongs.SongListManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MetadataEditViewModel(
    private val songFlow: Flow<LocalSong>,
    private val metadataEditor: MetadataEditor
) : ViewModel() {

    private val mSong = MutableLiveData<LocalSong>()
    val song: LiveData<LocalSong>
        get() = mSong

    init {
        viewModelScope.launch {
            songFlow.collect {
                mSong.value = it
            }
        }
    }

    class MetadataEditFactory @AssistedInject constructor(
        @Assisted("songUri") private val songUri: Uri,
        private val songListManager: SongListManager,
        private val metadataEditor: MetadataEditor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val songFlow = songListManager.songList.map {
                it.first { song -> song.uri == songUri }
            }.asFlow()
            return MetadataEditViewModel(songFlow, metadataEditor) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("songUri") songUri: Uri): MetadataEditFactory
        }
    }
}
