package com.nafanya.mp3world.core.listUtils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.utils.animators.AoedeAlphaAnimation
import com.nafanya.mp3world.core.view.BaseFragment
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.FragmentListContainerLayoutBinding
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import dagger.Lazy
import javax.inject.Inject

/**
 * Base class that renders recycler state and sets empty mock data.
 * Works correctly only for fragments that holds only three views - RecyclerView, loader and empty recycler mock.
 * Setting adapter and modifying it's data are successors responsibilities.
 */
@Suppress("TooManyFunctions")
abstract class StateFragment<DU, LI : BaseListItem> :
    BaseFragment<FragmentListContainerLayoutBinding>() {

    @Inject
    lateinit var factory: Lazy<ViewModelFactory>

    @Inject
    lateinit var favouriteListViewModel: Lazy<FavouriteListViewModel>

    abstract val adapter: BaseAdapter<LI, out BaseViewHolder>
    abstract val stateMachine: StateMachine<DU, LI>
    @get:DrawableRes
    abstract val emptyMockImageResource: Int
    @get:StringRes
    abstract val emptyMockTextResource: Int

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentListContainerLayoutBinding {
        return FragmentListContainerLayoutBinding.inflate(inflater, parent, attachToParent)
    }

    abstract fun onInject(applicationComponent: ApplicationComponent)

    override fun onAttach(context: Context) {
        onInject((requireActivity().application as PlayerApplication).applicationComponent)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(requireActivity() as AppCompatActivity) {
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            stateMachine.title.observe(this) {
                supportActionBar?.title = it
            }
        }
        stateMachine.listState.observe(viewLifecycleOwner, ::renderState)
        binding.emptyMock.emptyMockImage.setImageResource(emptyMockImageResource)
        binding.emptyMock.emptyMockText.text = context?.getText(emptyMockTextResource)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        stateMachine.listItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    protected open fun renderState(state: ListState) {
        when (state) {
            ListState.IS_LOADING -> renderIsLoading()
            ListState.IS_LOADED -> renderIsLoaded()
            ListState.IS_EMPTY -> renderIsEmpty()
            ListState.IS_UPDATED -> renderIsUpdated()
        }
    }

    protected open fun renderIsLoading() = with(binding) {
        recycler.isVisible = false
        emptyMock.root.isVisible = false
        loader.root.isVisible = true
    }

    protected open fun renderIsLoaded() = with(binding) {
        emptyMock.root.isVisible = false
        loader.root.isVisible = false
        recycler.isVisible = true
        recycler.startAnimation(AoedeAlphaAnimation())
    }

    protected open fun renderIsEmpty() = with(binding) {
        loader.root.isVisible = false
        recycler.isVisible = false
        emptyMock.root.isVisible = true
    }

    protected open fun renderIsUpdated() = with(binding) {
        emptyMock.root.isVisible = false
        loader.root.isVisible = false
        recycler.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            return true
        }
        return false
    }
}
