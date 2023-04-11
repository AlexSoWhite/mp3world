package com.nafanya.mp3world.features.metadataEditing

import android.content.IntentSender
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.common.StatedViewModel
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.allSongs.SongListManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.lang.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MetadataEditViewModel(
    private val songFlow: Flow<LocalSong?>,
    private val metadataEditor: MetadataEditor,
    private val iD3MetadataEditor: ID3MetadataEditor,
    private val jAudioTaggerEditor: JAudioTaggerEditor
) : StatedViewModel<LocalSong>() {

    private val mSong = MutableLiveData<LocalSong>()
    val song: LiveData<LocalSong>
        get() = mSong

    init {
        model.load {
            setDataSource(
                songFlow.map {
                    if (it != null) {
                        Data.Success(it)
                    } else {
                        Data.Error(Error("такой песни нет"))
                    }
                }
            )
        }
    }

    fun edit(song: LocalSong, senderCallback: (IntentSender) -> Unit) {
        model.refresh {
            metadataEditor.tryEdit(song) {
                senderCallback.invoke(it)
            }
        }
    }

    fun finishPendingEditing(song: LocalSong) {
        model.refresh {
            metadataEditor.edit(song)
        }
    }

    fun delete(song: LocalSong) {
        model.refresh {
            metadataEditor.delete(song)
        }
    }

    class MetadataEditFactory @AssistedInject constructor(
        @Assisted("songUri") private val songUri: Uri,
        private val songListManager: SongListManager,
        private val metadataEditor: MetadataEditor,
        private val iD3MetadataEditor: ID3MetadataEditor,
        private val jAudioTaggerEditor: JAudioTaggerEditor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val songFlow = songListManager.songList.map {
                it.firstOrNull { song -> song.uri == songUri }
            }.asFlow()
            return MetadataEditViewModel(
                songFlow,
                metadataEditor,
                iD3MetadataEditor,
                jAudioTaggerEditor
            ) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("songUri") songUri: Uri): MetadataEditFactory
        }
    }
}
