package com.nafanya.mp3world.features.songListViews.actionDialogs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.DialogOptionViewBinding

class DialogOptionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val binding = DialogOptionViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    val description = binding.optionMenuItemDescription
    val icon = binding.optionMenuItemIcon

    init {
        val attrs = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.DialogOptionView,
            defStyle,
            0
        )
        val iconDrawable = attrs.getDrawable(R.styleable.DialogOptionView_icon)
        val text = attrs.getString(R.styleable.DialogOptionView_description)
        icon.setImageDrawable(iconDrawable)
        description.text = text
        description.isSelected = true
        attrs.recycle()
    }
}
