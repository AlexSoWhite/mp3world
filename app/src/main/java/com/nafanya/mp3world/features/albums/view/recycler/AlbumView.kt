package com.nafanya.mp3world.features.albums.view.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseListItemView
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.databinding.AlbumViewBinding
import com.nafanya.mp3world.features.albums.Album

class AlbumView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : BaseListItemView(context, attributeSet, defStyle) {

    private val binding: AlbumViewBinding =
        AlbumViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setAlbum(album: Album, onClickCallback: () -> Unit) {
        binding.albumName.text = album.name
        binding.tracksCount.text = TextUtil.getCompositionsCountString(
            album.playlist!!.songList.size
        )
        if (album.image != null) {
            binding.albumImage.setImageBitmap(album.image)
        } else {
            binding.albumImage.setImageResource(R.drawable.album)
        }
        binding.albumListItem.setOnClickListener {
            onClickCallback()
        }
    }
}
