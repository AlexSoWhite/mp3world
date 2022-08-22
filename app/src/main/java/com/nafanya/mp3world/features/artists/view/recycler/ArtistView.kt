package com.nafanya.mp3world.features.artists.view.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseListItemView
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.databinding.PlaylistContainerViewBinding
import com.nafanya.mp3world.features.artists.Artist

class ArtistView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle) {

    private val binding: PlaylistContainerViewBinding =
        PlaylistContainerViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setArtist(artist: Artist, onClickCallback: () -> Unit) = binding.apply {
        itemTitle.text = artist.name
        tracksCount.text = TextUtil.getCompositionsCountString(
            artist.playlist!!.songList.size
        )
        if (artist.image != null) {
            itemImage.setImageBitmap(artist.image)
        } else {
            itemImage.setImageResource(R.drawable.artist)
        }
        setOnClickListener {
            onClickCallback()
        }
    }
}

class ArtistViewHolder(private val view: ArtistView) : BaseViewHolder(view) {

    fun bind(artist: Artist, onClick: () -> Unit) {
        view.setArtist(artist, onClick)
    }
}
