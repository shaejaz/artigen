package com.shaejaz.artigen.utils

import android.text.InputFilter
import android.text.Spanned

class NumberFilter : InputFilter {
    private var mIntMin: Float
    private var mIntMax: Float

    constructor(minValue: Int, maxValue: Int) {
        mIntMin = minValue.toFloat()
        mIntMax = maxValue.toFloat()
    }

    constructor(minValue: Float, maxValue: Float) {
        mIntMin = minValue
        mIntMax = maxValue
    }

    constructor(minValue: String, maxValue: String) {
        mIntMin = minValue.toFloat()
        mIntMax = maxValue.toFloat()
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toFloat()
            if (isInRange(mIntMin, mIntMax, input)) return null
        } catch (e: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(a: Float, b: Float, c: Float): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}