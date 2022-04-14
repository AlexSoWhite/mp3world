package com.nafanya.mp3world.view

import android.content.Context
import android.content.Intent
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider

class ActivityCreator {

    private lateinit var context: Context
    private lateinit var intent: Intent

    fun with(context: Context): ActivityCreator {
        this.context = context
        return this
    }

    /**
     * TODO: inject playlist
     */
    fun createActivityWithPlaylist(
        playlist: Playlist,
        activityClass: Class<*>?
    ): ActivityCreator {
        intent = Intent(context, activityClass)
        SourceProvider.newInstanceWithPlaylist(playlist)
        return this
    }

    fun createActivityWithQuery(
        query: String,
        activityClass: Class<*>?
    ): ActivityCreator {
        intent = Intent(context, activityClass)
        SourceProvider.newInstanceWithQuery(query)
        return this
    }

    fun start() {
        context.startActivity(intent)
    }
}
