package com.nafanya.mp3world.view.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.LegacyRecyclerHolderFragmentBinding
import com.nafanya.mp3world.model.OnSwipeListener

@Deprecated(message = "Use RecyclerHolderActivity instead")
abstract class RecyclerHolderFragment : Fragment(R.layout.legacy_recycler_holder_fragment) {

    protected lateinit var binding: LegacyRecyclerHolderFragmentBinding
    protected lateinit var onSwipeListener: OnSwipeListener
    protected lateinit var onScrollListener: ScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.legacy_recycler_holder_fragment,
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
        addCustomBehavior()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onScrollListener = ScrollListener(
            requireActivity().resources.displayMetrics.density,
            binding
        )
        onSwipeListener = OnSwipeListener(binding.recycler) { onScrollListener.shrink() }
        binding.recycler.addOnScrollListener(onScrollListener)
    }

    abstract fun addCustomBehavior()

    abstract fun setAdapter()

    abstract fun getItemCount(): Int

    abstract fun getFragmentDescription(): String
}
