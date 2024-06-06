package com.nafanya.mp3world.core.utils.listUtils.searching

import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.nafanya.mp3world.R

/**
 * Interface for adding search functionality. To use it setup a [SearchProcessor].
 * UI elements, that are managed by a [Searchable] viewModel can use [attachToTopBar] extension
 * or implement a searching UI in place
 */
interface Searchable<DU> {

    fun search(query: String)
}

/**
 * Extension method to invoke an a [Searchable] viewModel inside a
 * [AppCompatActivity.onCreateOptionsMenu] or [Fragment.onCreateOptionsMenu] to add search ui
 */
fun <T> Searchable<T>.attachToTopBar(): (menu: Menu, inflater: MenuInflater) -> Unit {
    return { menu, inflater ->
        // setting appBar search view
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(DefaultOnQueryTextListener(this))
    }
}

class DefaultOnQueryTextListener<DU>(
    private val searchable: Searchable<DU>
) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String): Boolean {
        searchable.search(query)
        return false
    }
    override fun onQueryTextChange(newText: String): Boolean {
        searchable.search(newText)
        return false
    }
}
