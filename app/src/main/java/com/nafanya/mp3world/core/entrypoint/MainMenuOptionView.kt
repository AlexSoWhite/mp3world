package com.nafanya.mp3world.core.entrypoint

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.MainMenuItemViewBinding

class MainMenuOptionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val binding = MainMenuItemViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    val count = binding.count

    init {
        val attrs = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MainMenuOptionView,
            defStyle,
            0
        )
        val iconDrawable = attrs.getDrawable(R.styleable.MainMenuOptionView_icon)
        val text = attrs.getString(R.styleable.MainMenuOptionView_description)
        binding.menuItemIcon.setImageDrawable(iconDrawable)
        binding.description.text = text
        attrs.recycle()
    }
}
