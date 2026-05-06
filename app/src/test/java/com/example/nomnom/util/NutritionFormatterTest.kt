package com.example.nomnom.util

import org.junit.Assert.assertEquals
import org.junit.Test

class NutritionFormatterTest {

    @Test
    fun `formatCalories should add Calories suffix`() {
        val result = NutritionFormatter.formatCalories("100")
        assertEquals("100 Calories", result)
    }

    @Test
    fun `formatGram should add g suffix`() {
        val result = NutritionFormatter.formatGram("25")
        assertEquals("25g", result)
    }

    @Test
    fun `formatMilligram should add mg suffix`() {
        val result = NutritionFormatter.formatMilligram("500")
        assertEquals("500mg", result)
    }

    @Test
    fun `formatCalories should return 0 Calories for null`() {
        val result = NutritionFormatter.formatCalories(null)
        assertEquals("0 Calories", result)
    }

    @Test
    fun `formatGram should return 0g for null`() {
        val result = NutritionFormatter.formatGram(null)
        assertEquals("0g", result)
    }
}
