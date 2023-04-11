package com.nafanya.mp3world.features.metadataEditing

import android.util.Log
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey

class JAudioTaggerEditor @Inject constructor(
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mediaStoreInteractor: MediaStoreInteractor
) {

    @Suppress("TooGenericExceptionCaught")
    fun edit(song: LocalSong) {
        ioCoroutineProvider.ioScope.launch {
            kotlin.runCatching {
                try {
                    val file = AudioFileIO.read(File(song.path))
                    val tag = file.tag
                    tag.setField(FieldKey.TITLE, song.title)
                    tag.setField(FieldKey.ARTIST, song.artist)
                    tag.setField(FieldKey.ALBUM, song.album)
                    file.commit()
                    mediaStoreInteractor.reset()
                } catch (exception: Exception) {
                    exception.localizedMessage?.let { Log.d("EDIT", it) }
                }
            }
        }
    }
}
