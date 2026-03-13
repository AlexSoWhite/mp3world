package com.nafanya.mp3world.presentation.song_list_views.action_dialogs

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.song.ArtistMetadata
import com.nafanya.mp3world.core.wrappers.song.joinArtists

@Composable
fun LocalSongActionDialogView(
    modifier: Modifier = Modifier,
    songTitle: String,
    songArtists: List<ArtistMetadata>,
    songAlbum: String,
    isFavorite: Boolean,
    onAction: (LocalSongAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color("#373232".toColorInt()))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.basicMarquee(
                velocity = 60.dp
            ).padding(start = 16.dp, end = 16.dp),
            color = Color.White,
            fontSize = 18.sp,
            text = "${songArtists.joinArtists()} - $songTitle",
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        SongAction(
            icon = R.drawable.icv_album,
            text = songAlbum,
            onAction = { onAction(LocalSongAction.GoToAlbum) }
        )

        songArtists.forEach { artist ->
            SongAction(
                icon = R.drawable.icv_artist,
                text = artist.name,
                onAction = { onAction(LocalSongAction.GoToArtist(artist)) }
            )
        }

        if (isFavorite) {
            SongAction(
                icon = R.drawable.icv_favorite_filled,
                text = stringResource(R.string.remove_from_favourites),
                onAction = { onAction(LocalSongAction.RemoveFromFavorite) }
            )
        } else {
            SongAction(
                icon = R.drawable.icv_favorite_border,
                text = stringResource(R.string.add_to_favourites),
                onAction = { onAction(LocalSongAction.AddToFavorite) }
            )
        }
    }
}

@Composable
private fun SongAction(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onAction: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onAction
            )
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(1f).basicMarquee(),
            text = text,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun LocalSongActionDialogPreview() {
    MaterialTheme {
        LocalSongActionDialogView(
            songTitle = "Memory Reboot",
            songArtists = listOf(
                ArtistMetadata(id = 0, name = "VOJ"),
                ArtistMetadata(id = 1, name = "Narvent")
            ),
            songAlbum = "Download",
            isFavorite = false
        ) {}
    }
}
