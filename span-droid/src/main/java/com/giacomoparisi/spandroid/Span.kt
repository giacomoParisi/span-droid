package com.giacomoparisi.spandroid

import android.content.Context
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

sealed class Span {

    abstract fun span(): Any

    data class Appearance(
        @StyleRes private val styleId: Int,
        private val context: Context
    ) : Span() {
        override fun span(): Any =
            TextAppearanceSpan(context, styleId)
    }

    data class Size(
        @DimenRes private val dimenId: Int,
        private val context: Context
    ) : Span() {
        override fun span(): Any =
            AbsoluteSizeSpan(context.resources.getDimension(dimenId).toInt())
    }

    data class Color(
        @ColorRes private val colorId: Int,
        private val context: Context
    ) : Span() {
        override fun span(): Any =
            ForegroundColorSpan(ContextCompat.getColor(context, colorId))
    }

    data class Icon(
        @DrawableRes private val imageId: Int,
        private val size: Int,
        private val context: Context
    ) : Span() {
        override fun span(): Any =
            ImageSpan(
                AppCompatResources.getDrawable(context, imageId)!!
                    .also { it.setBounds(0, 0, size, size) }
            )
    }

    data class Typeface(
        @FontRes private val fontId: Int,
        private val context: Context
    ) : Span() {
        override fun span(): Any {

            val typeface = ResourcesCompat.getFont(context, fontId)

            return object : MetricAffectingSpan() {

                override fun updateDrawState(paint: TextPaint) {
                    paint.typeface = typeface
                }

                override fun updateMeasureState(paint: TextPaint) {
                    paint.typeface = typeface
                }
            }
        }
    }

    object StrikeThrough : Span() {
        override fun span(): Any = StrikethroughSpan()
    }

    object Underline : Span() {
        override fun span(): Any = UnderlineSpan()
    }

    data class Click(
        private val action: () -> Unit
    ) : Span() {
        override fun span(): Any =
            object : ClickableSpan() {

                override fun onClick(view: View): Unit = action()

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
    }

    data class Custom(
        private val span: Any
    ) : Span() {
        override fun span(): Any = span
    }

}