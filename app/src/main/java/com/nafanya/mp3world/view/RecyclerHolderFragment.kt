package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.RecycleHolderFragmentBinding
import com.nafanya.mp3world.model.OnSwipeListener

abstract class RecyclerHolderFragment : Fragment(R.layout.recycle_holder_fragment) {

    protected lateinit var binding: RecycleHolderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.recycle_holder_fragment,
            container,
            false
        )
        setAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.itemCount = getItemCount()
        binding.fragmentDescription = getFragmentDescription()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scrollListener = ScrollListener(
            requireActivity(),
            binding.recycler,
            binding.recycler.background
        )
        OnSwipeListener(binding.recycler) { scrollListener.shrink() }
        binding.recycler.addOnScrollListener(scrollListener)
    }

    abstract fun setAdapter()

    abstract fun getItemCount(): Int

    abstract fun getFragmentDescription(): String
}
