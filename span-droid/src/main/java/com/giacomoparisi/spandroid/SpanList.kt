package com.giacomoparisi.spandroid

import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * Class that represent a list of spans
 */
class SpanList(private val context: Context) : Iterable<Any> {

    private val spans = ArrayList<Any>()

    override fun iterator(): MutableIterator<Any> = spans.iterator()

    /**
     * Add a TextAppearanceSpan to this SpanList instance
     * @param id The id of the style to apply
     */
    fun appearance(@StyleRes id: Int): Boolean =
            spans.add(TextAppearanceSpan(context, id))

    /**
     * Add a AbsoluteSizeSpan to this SpanList instance
     * @param id The id of the dimen resource to apply
     */
    fun size(@DimenRes id: Int): Boolean =
            spans.add(AbsoluteSizeSpan(context.resources.getDimension(id).toInt()))

    /**
     * Add a ForegroundColorSpan to this SpanList instance
     * @param id The id of the color to apply
     */
    fun color(@ColorRes id: Int): Boolean =
            spans.add(ForegroundColorSpan(ContextCompat.getColor(context, id)))

    /**
     * Add a ImageSpan to this SpanList instance
     * @param id The resource id of the icon
     * @param size The desired size of the icon
     */
    fun icon(@DrawableRes id: Int, size: Int): Boolean =
            spans.add(
                    ImageSpan(AppCompatResources.getDrawable(context, id)!!
                            .also {
                                it.setBounds(0, 0, size, size)
                            }))

    /**
     * Add a TypefaceSpan with sans-serif-medium family to this SpanList instance
     */
    fun sansSerifMedium(): Boolean = typeface("sans-serif-medium")

    /**
     * Add a TypefaceSpan with sans-serif-regular family to this SpanList instance
     */
    fun sansSerifRegular(): Boolean = typeface("sans-serif-regular")

    /**
     * Add a TypefaceSpan to this SpanList instance
     * @param family The family string of the typeface to apply
     */
    fun typeface(family: String): Boolean = spans.add(TypefaceSpan(family))

    /**
     * Add a TypefaceSpan to this SpanList instance
     * @param fontId The id of the font res to apply
     */
    fun typeface(@FontRes fontId: Int): Boolean =
            spans.add(CustomTypefaceSpan(ResourcesCompat.getFont(context, fontId)))

    /**
     * Add a StrikethroughSpan to this SpanList instance
     */
    fun strikeThrought(): Boolean =
            spans.add(StrikethroughSpan())

    /**
     * Add a ClickableSpan to this SpanList instance
     * that execute the action method when the span is clicked
     * @param action The action to be performed when the user click on the span
     */
    fun click(action: () -> Unit): Boolean = spans.add(clickableSpan(action))

    /**
     * Add a custom span to this SpanList instance
     * @param span The custom span instance to add
     */
    fun custom(span: Any): Boolean = spans.add(span)
}

/**
 * Builder method for ClickableSpan class
 * @param action The action to be performed when the user click on the span
 * @return The ClickableSpan with the action function mapped to the onClick method
 */
fun clickableSpan(action: () -> Unit): ClickableSpan = object : ClickableSpan() {

    override fun onClick(view: View): Unit = action()

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}


/**
 * Builder method for SpanList class
 * @param builder Lambda function for the SpanList instance
 * @return The Span instance
 */
fun spanList(context: Context, builder: SpanList.() -> Unit): SpanList =
        SpanList(context).apply(builder)

/**
 * Class that allow the utilization of custom font res
 */
class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}