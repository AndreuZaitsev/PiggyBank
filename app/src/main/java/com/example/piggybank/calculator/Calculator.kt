package com.example.piggybank.calculator

import javax.inject.Inject

class Calculator @Inject constructor() {

    fun calculate(input: String): Double {
        val numbers = input.split("-", "+").map { it.toDoubleOrNull() ?: 0.0 }

        val symbols = input
            .toCharArray()
            .filter {
                it == '+' || it == '-'
            }
            .toMutableList()
        symbols.add(0, '+')

        var sum = 0.0
        for ((i, num) in numbers.withIndex()) {
            if (symbols[i] == '-') {
                sum -= num
            } else {
                sum += num
            }
        }
        return String.format("%.2f", sum).toDouble()
    }
}