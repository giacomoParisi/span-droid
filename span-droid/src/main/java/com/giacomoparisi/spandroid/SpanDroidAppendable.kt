package com.giacomoparisi.spandroid

import android.content.res.Resources
import android.os.Build
import androidx.annotation.StringRes
import java.util.*

class SpanDroidAppendable(
    private val creator: SpanDroid,
    vararg spanParts: Pair<Any, Iterable<Any>>) : Appendable {

    private val spansMap =
            spanParts.toMap()
                    .mapKeys { entry ->
                        entry.key.let {
                            it as? CharSequence ?: it.toString()
                        }
                    }

    override fun append(csq: CharSequence?) =
            apply { creator.appendSmart(csq, spansMap) }

    override fun append(csq: CharSequence?, start: Int, end: Int) =
            apply {
                if (csq != null) {
                    if (start in 0 until end && end <= csq.length) {
                        append(csq.subSequence(start, end))
                    } else {
                        throw IndexOutOfBoundsException(
                                "start " +
                                        start +
                                        ", end " +
                                        end +
                                        ", s.length() " +
                                        csq.length)
                    }
                }
            }

    override fun append(c: Char) = apply { creator.append(c.toString()) }

    private fun SpanDroid.appendSmart(
            csq: CharSequence?,
            spanDict: Map<CharSequence, Iterable<Any>>
    ) {
        if (csq != null) {
            if (csq in spanDict) {
                append(csq, spanDict.getValue(csq))
            } else {
                val possibleMatchDict = spanDict.filter { it.key.toString() == csq }
                if (possibleMatchDict.isNotEmpty()) {
                    val spanDictEntry = possibleMatchDict.entries.toList()[0]
                    append(spanDictEntry.key, spanDictEntry.value)
                } else {
                    append(csq)
                }
            }
        }
    }
}

fun Resources.getSpannable(
        @StringRes id: Int,
        vararg spanParts: Pair<Any, Iterable<Any>>
): CharSequence {
    val resultCreator = SpanDroid()
    val locale =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.locales.get(0)
            } else {
                configuration.locale
            }
    Formatter(
            SpanDroidAppendable(resultCreator, *spanParts),
            locale)
            .format(getString(id), *spanParts.map { it.first }.toTypedArray())
    return resultCreator.toSpannableString()
}

fun Resources.getText(@StringRes id: Int, vararg formatArgs: Any?) =
        getSpannable(id, *formatArgs.filterNotNull().map { it to emptyList<Any>() }.toTypedArray())