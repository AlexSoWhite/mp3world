package com.nafanya.mp3world.core.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Base ViewBinding Activity class.
 * Takes care of inflating view.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var mBinding: VB
    protected val binding
        get() = mBinding

    abstract fun inflate(
        layoutInflater: LayoutInflater
    ): VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = inflate(layoutInflater)
        setContentView(binding.root)
    }
}
