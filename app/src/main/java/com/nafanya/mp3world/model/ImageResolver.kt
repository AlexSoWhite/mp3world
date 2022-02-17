package com.nafanya.mp3world.model

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nafanya.mp3world.model.wrappers.Song

// TODO make it work
object ImageResolver {

    fun songImage(song: Song, view: ImageView) {
        val retriever = MediaMetadataRetriever()
        // retriever.setDataSource(song.path)
        val art = retriever.embeddedPicture
        if (art != null) {
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            Glide.with(view).load(bitmap).into(view)
        } else {
            Glide.with(view).load(null).into(view)
        }
    }
}
