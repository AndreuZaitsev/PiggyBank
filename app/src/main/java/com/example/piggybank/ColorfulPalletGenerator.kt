package com.example.piggybank

import android.graphics.Color
import javax.inject.Inject

class ColorfulPalletGenerator @Inject constructor() {

    fun generateColors(number: Int): List<Int> {
        val colors: MutableList<Int> = mutableListOf()
        val hStep = if (number > 0) 360 / number else 10
        var h = 0
        var s = 100
        var v = 100
        for (num in 0 until number) {
            val color: Int = Color.HSVToColor(floatArrayOf(h.toFloat(), s.toFloat(), v.toFloat()))
            colors += color

            val nextH = h + hStep
            when {
                nextH < 360 -> h = nextH
                s > 20 -> {
                    h = 0
                    s -= 20
                }
                v > 20 -> {
                    h = 0
                    s = 100
                    v -= 20
                }
                else -> {
                    h = 0
                    s = 100
                    v = 100
                }
            }
        }
        return colors
    }
}