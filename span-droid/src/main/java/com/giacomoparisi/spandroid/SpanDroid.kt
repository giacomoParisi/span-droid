package com.giacomoparisi.spandroid

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextUtils.concat

/**
 * Creator is responsible only for building a spannable string,
 * so it provides pack of append-methods for doing it nicely.
 * With this creator we able to construct our spannable string part by part and also,
 * assign special spans for each part.
 */
class SpanDroid private constructor() {

    private val parts = ArrayList<CharSequence>()
    private var length = 0
    private val spanMap: MutableMap<IntRange, Iterable<Any>> = HashMap()

    fun appendSpace(newText: CharSequence): SpanDroid =
        this.append(" ").append(newText)

    fun appendSpace(newText: CharSequence, vararg spans: Span): SpanDroid =
        this.append(" ").append(newText, *spans)

    fun appendLnNotBlank(newText: CharSequence, vararg spans: Span): SpanDroid =
        this.applyIf({ newText.isNotBlank() }) { this.appendLn(newText, *spans) }

    fun appendLn(newText: CharSequence, vararg spans: Span): SpanDroid =
        this.append("\n").append(newText, *spans)

    fun append(newText: CharSequence, vararg spans: Span): SpanDroid =
        apply {
            val end = newText.length
            this.parts.add(newText)
            this.spanMap[(length..length + end)] = spans.toList()
            this.length += end
        }

    fun append(newText: CharSequence): SpanDroid =
        apply {
            this.parts.add(newText)
            this.length += newText.length
        }

    inline fun applyIf(
        predicate: () -> Boolean,
        action: SpanDroid.() -> SpanDroid
    ): SpanDroid =
        if (predicate()) action() else this

    fun toSpannableString(): SpannableString =
        SpannableString(concat(*parts.toTypedArray()))
            .also { spannableString ->
                spanMap.forEach { entry ->
                    val range = entry.key
                    entry.value.forEach {
                        spannableString.setSpan(
                            it,
                            range.first,
                            range.last,
                            SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

    companion object {

        fun span(): SpanDroid = SpanDroid()

    }

}