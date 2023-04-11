package com.nafanya.mp3world.features.metadataEditing

import android.app.RecoverableSecurityException
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import kotlinx.coroutines.launch

class MetadataEditor @Inject constructor(
    private val context: Context,
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val metadataExtractor: MetadataExtractor
) {

    @Suppress("TooGenericExceptionThrown")
    fun tryEdit(song: LocalSong, senderCallBack: (IntentSender) -> Unit) {
        try {
            context.contentResolver.openFileDescriptor(song.uri, "w")?.use {
                edit(song)
            }
        } catch (exception: SecurityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val recoverableSecurityException = exception as?
                    RecoverableSecurityException
                    ?: throw RuntimeException(exception.message, exception)

                val intentSender =
                    recoverableSecurityException.userAction.actionIntent.intentSender
                senderCallBack.invoke(intentSender)
            } else {
                throw RuntimeException(exception.message, exception)
            }
        }
    }

    fun edit(song: LocalSong) {
        ioCoroutineProvider.ioScope.launch {
            val contentValues = metadataExtractor.getContentValues(song)
            contentValues.put(MediaStore.Audio.Media.ARTIST, song.artist)
            contentValues.put(MediaStore.Audio.Media.ALBUM, song.album)
            contentValues.put(MediaStore.Audio.Media.TITLE, song.title)
            val result = context.contentResolver.update(
                song.uri,
                contentValues,
                null,
                null
            )
            if (result > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.contentResolver.refresh(song.uri, null, null)
                } else {
                    context.contentResolver.notifyChange(song.uri, null)
                }
                mediaStoreInteractor.reset()
            }
        }
    }

    fun delete(song: LocalSong) {
        ioCoroutineProvider.ioScope.launch {
            context.contentResolver.delete(
                song.uri,
                null,
                null
            )
            mediaStoreInteractor.reset()
        }
    }
}
