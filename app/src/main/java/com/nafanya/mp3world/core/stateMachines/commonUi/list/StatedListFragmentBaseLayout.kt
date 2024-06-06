package com.nafanya.mp3world.core.stateMachines.commonUi.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.core.utils.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.databinding.FragmentListContainerLayoutBinding

abstract class StatedListFragmentBaseLayout<DU, LI : BaseListItem> : StatedListFragment<
    FragmentListContainerLayoutBinding,
    DU,
    LI
    >() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(requireActivity() as AppCompatActivity) {
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.emptyMock.apply {
            emptyMockImage.setImageResource(emptyMockImageResource)
            emptyMockText.text = requireContext().getText(emptyMockTextResource)
        }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun renderError(error: Error) {
        renderEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Suppress("EmptyFunctionBlock")
    override fun renderInitializing() {}

    override fun renderLoading() = with(binding) {
        loader.root.isVisible = true
        recycler.isVisible = false
        emptyMock.root.isVisible = false
    }

    override fun renderEmpty() = with(binding) {
        super.renderEmpty()
        loader.root.isVisible = false
        recycler.isVisible = false
        emptyMock.root.isVisible = true
    }

    override fun renderUpdated(data: List<DU>) = with(binding) {
        super.renderUpdated(data)
        loader.root.isVisible = false
        recycler.isVisible = true
        emptyMock.root.isVisible = false
    }

    override fun renderSuccess(data: List<DU>) = with(binding) {
        super.renderSuccess(data)
        loader.root.isVisible = false
        recycler.isVisible = true
        emptyMock.root.isVisible = false
    }
}
