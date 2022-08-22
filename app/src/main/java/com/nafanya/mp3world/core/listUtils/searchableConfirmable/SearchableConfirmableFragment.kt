package com.nafanya.mp3world.core.listUtils.searchableConfirmable

import android.view.Menu
import android.view.MenuInflater
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist.DiscardChangesDialog

interface SearchableConfirmableFragment<DU> {

    fun createTopBar(
        fragment: Fragment,
        searchable: Searchable<DU>,
        onConfirmCallback: () -> Unit
    ): (menu: Menu, inflater: MenuInflater) -> Boolean {
        return { menu, inflater ->
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val dialog = DiscardChangesDialog(
                        fragment.requireActivity()
                    ) {
                        fragment.requireActivity().finish()
                    }
                    dialog.show()
                }
            }
            fragment.requireActivity().onBackPressedDispatcher.addCallback(
                fragment.viewLifecycleOwner,
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
