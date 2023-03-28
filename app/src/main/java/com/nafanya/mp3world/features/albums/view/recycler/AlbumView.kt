package com.nafanya.mp3world.features.albums.view.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseListItemView
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.databinding.PlaylistContainerViewBinding
import com.nafanya.mp3world.features.albums.Album

class AlbumView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle) {

    private val binding: PlaylistContainerViewBinding =
        PlaylistContainerViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setAlbum(album: Album, onClickCallback: () -> Unit) = binding.apply {
        itemTitle.text = album.name
        tracksCount.text = TextUtil.getCompositionsCountString(
            album.playlist!!.songList.size
        )
        if (album.image != null) {
            itemImage.setImageBitmap(album.image)
        } else {
            itemImage.setImageResource(R.drawable.icv_album)
        }
        setOnClickListener {
            onClickCallback()
        }
    }
}
