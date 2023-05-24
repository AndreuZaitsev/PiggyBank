package com.example.piggybank

import com.example.piggybank.calculator.Calculator
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CalculatorTest {

    private val sut = Calculator()

    @Test
    fun addition_isCorrect() {
        val input = "-2+2-1+-"
        val sum = sut.calculate(input)

        assertEquals(-1.0, sum, 0.0)
    }
}