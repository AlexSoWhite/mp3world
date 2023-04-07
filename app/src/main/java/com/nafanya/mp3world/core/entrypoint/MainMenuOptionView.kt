package com.nafanya.mp3world.core.entrypoint

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.StateModel
import com.nafanya.mp3world.databinding.MainMenuItemViewBinding
import java.lang.IllegalArgumentException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainMenuOptionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    companion object {
        const val ALL_SONGS = 0
        const val ALL_PLAYLISTS = 1
        const val ARTISTS = 2
        const val ALBUMS = 3
        const val FAVOURITES = 4
    }

    private val binding = MainMenuItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        val attrs = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MainMenuOptionView,
            defStyle,
            0
        )
        val iconDrawable = attrs.getDrawable(R.styleable.MainMenuOptionView_icon)
        val text = attrs.getString(R.styleable.MainMenuOptionView_description)
        val starterBuilder = ActivityStarter.Builder()
            .with(context)
        when (val option = attrs.getInt(R.styleable.MainMenuOptionView_option, -1)) {
            ALL_SONGS -> starterBuilder.createIntentToAllSongsActivity()
            ALL_PLAYLISTS -> starterBuilder.createIntentToAllPlaylistsActivity()
            ARTISTS -> starterBuilder.createIntentToArtistListActivity()
            ALBUMS -> starterBuilder.createIntentToAlbumListActivity()
            FAVOURITES -> starterBuilder.createIntentToFavouritesActivity()
            else -> throw IllegalArgumentException(
                "Unexpected option $option. Did you forget to specify option in the layout file?"
            )
        }
        val activityStarter = starterBuilder.build()
        binding.root.setOnClickListener { activityStarter.startActivity() }
        binding.menuItemIcon.setImageDrawable(iconDrawable)
        binding.description.text = text
        attrs.recycle()
    }

    fun bindDataSource(
        model: StateModel<Int>,
        scope: CoroutineScope
    ) {
        scope.launch {
            model.currentState.collect {
                renderState(it)
            }
        }
    }

    private fun renderState(state: State<Int>) {
        when (state) {
            is State.Loading -> {
                binding.mainMenuProgress.isVisible = true
                binding.count.isInvisible = true
            }
            is State.Success -> {
                binding.mainMenuProgress.isVisible = false
                binding.count.isInvisible = false
                binding.count.text = state.data.toString()
            }
            is State.Updated -> {
                binding.mainMenuProgress.isVisible = false
                binding.count.isInvisible = false
                binding.count.text = state.data.toString()
            }
            else -> { }
        }
    }
}
