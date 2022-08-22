package com.nafanya.mp3world.core.listUtils.searchableConfirmable

import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.searching.Searchable

interface SearchableConfirmableFragment<DU> {

    fun createTopBar(
        searchable: Searchable<DU>,
        onConfirmCallback: () -> Unit
    ): (menu: Menu, inflater: MenuInflater) -> Boolean {
        return { menu, inflater ->
            // setting appBar search view
            inflater.inflate(R.menu.add_songs_to_playlist_menu, menu)
            val confirm = menu.findItem(R.id.confirm_adding)
            val searchItem = menu.findItem(R.id.search)
            val searchView: SearchView = searchItem?.actionView as SearchView
            // setting search dispatcher
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchable.search(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        searchable.search(newText)
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
}
