package com.example.piggybank

class Calculator {

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
        return sum
    }
}