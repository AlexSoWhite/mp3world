package com.nafanya.mp3world.core.listUtils.searching

import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import com.nafanya.mp3world.R

/**
 * Interface for [androidx.fragment.app.Fragment] that allows to add [SearchView]
 * to top bar. This view will trigger Fragment's [Searchable] to update query. To use searching functionality
 * invoke [createTopBar] inside the [androidx.fragment.app.Fragment.onCreateOptionsMenu].
 */
interface SearchableFragment<DU> {

    fun createTopBar(searchable: Searchable<DU>): (menu: Menu, inflater: MenuInflater) -> Boolean {
        return { menu, inflater ->
            // setting appBar search view
            inflater.inflate(R.menu.search_menu, menu)
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
            true
        }
    }
}
