package com.example.piggybank

import android.graphics.Color
import javax.inject.Inject

class ColorfulPalletGenerator @Inject constructor(
    private val colorProvider: IColorProvider
) {

    fun generateColors(number: Int): List<Int> {
        val colors: MutableList<Int> = mutableListOf()
        val hStep = if (number > 0) 360 / number else 10
        var h = 0
        var s = 100
        var v = 100
        for (num in 0 until number) {
            val color: Int = colorProvider.getColor(h, s, v)
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
interface IColorProvider{
    fun getColor(h: Int, s: Int, v: Int): Int
}
    class ColorProvider @Inject constructor(): IColorProvider {
        override fun getColor(h: Int, s: Int, v: Int):Int {
          return Color.HSVToColor(floatArrayOf(h.toFloat(), s.toFloat(), v.toFloat()))
        }
    }
}