package com.nafanya.mp3world.core.utils

import android.view.Menu
import android.view.MenuInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist.DiscardChangesDialog

fun <T> SearchableStated<T>.attachToTopBar(): (menu: Menu, inflater: MenuInflater) -> Boolean {
    return { menu, inflater ->
        // setting appBar search view
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    search(query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    search(newText)
                    return false
                }
            }
        )
        true
    }
}

fun <T> SearchableStated<T>.attachToConfirmableTopBar(
    activity: AppCompatActivity,
    onConfirmCallback: () -> Unit
): (Menu, MenuInflater) -> Boolean {
    return { menu, inflater ->
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = DiscardChangesDialog(
                    activity
                ) {
                    activity.finish()
                }
                dialog.show()
            }
        }
        activity.onBackPressedDispatcher.addCallback(
            activity,
            callback
        )
        // setting appBar search view
        inflater.inflate(R.menu.add_songs_to_playlist_menu, menu)
        val confirm = menu.findItem(R.id.confirm_adding)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    search(query)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    search(newText)
                    return false
                }
            }
        )
        confirm?.setOnMenuItemClickListener {
            onConfirmCallback()
            true
        }
        true
    }
}
