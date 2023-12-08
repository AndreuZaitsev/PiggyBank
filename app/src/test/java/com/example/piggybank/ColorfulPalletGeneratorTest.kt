package com.example.piggybank

import com.example.piggybank.ColorfulPalletGenerator.IColorProvider
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.mock

class ColorfulPalletGeneratorTest {

    private val mockColorProvider: IColorProvider = mock {
        on { getColor(anyInt(), anyInt(), anyInt()) }.thenReturn(0)
    }
    private val sut = ColorfulPalletGenerator(mockColorProvider)

    @Test
    fun generateColors_returnedListOfColors() {
        val colors: List<Int> = sut.generateColors(2)
        assertThat("", colors.size == 2)
        assertEquals(2, colors.size)
    }
}