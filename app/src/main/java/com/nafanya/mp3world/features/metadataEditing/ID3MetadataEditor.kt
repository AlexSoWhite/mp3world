package com.nafanya.mp3world.features.metadataEditing

import android.util.Log
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.cmc.music.common.ID3WriteException
import org.cmc.music.metadata.MusicMetadata
import org.cmc.music.metadata.MusicMetadataSet
import org.cmc.music.myid3.MyID3

class ID3MetadataEditor @Inject constructor(
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val iD3: MyID3
) {

    fun edit(song: LocalSong) {
        ioCoroutineProvider.ioScope.launch {
            val source = File(song.path)
            var sourceSet: MusicMetadataSet
            kotlin.runCatching {
                try {
                    sourceSet = iD3.read(source)
                    val data = MusicMetadata(song.title)
                    data.songTitle = song.title
                    data.artist = song.artist
                    data.album = song.album
                    try {
                        iD3.update(source, sourceSet, data)
                        mediaStoreInteractor.reset()
                    } catch (exception: UnsupportedEncodingException) {
                        exception.localizedMessage?.let { Log.d("EDIT", it) }
                    } catch (exception: ID3WriteException) {
                        exception.localizedMessage?.let { Log.d("EDIT", it) }
                    } catch (exception: IOException) {
                        exception.localizedMessage?.let { Log.d("EDIT", it) }
                    }
                } catch (exception: IOException) {
                    exception.localizedMessage?.let { Log.d("EDIT", it) }
                }
            }
        }
    }
}
