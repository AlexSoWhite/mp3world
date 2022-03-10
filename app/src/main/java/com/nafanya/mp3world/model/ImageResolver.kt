package com.nafanya.mp3world.model

import android.media.MediaMetadataRetriever
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.wrappers.Song

object ImageResolver {

    fun songImage(song: Song, view: ImageView) {
        val retriever = MediaMetadataRetriever()
        if (song.path != null) {
            retriever.setDataSource(song.path)
            val art = retriever.embeddedPicture
            if (art != null) {
                Glide.with(view).load(art).into(view)
            } else {
                Glide.with(view).load(R.drawable.equalizer).into(view)
            }
        } else {
            Glide.with(view).load(R.drawable.equalizer).into(view)
        }
    }
}
