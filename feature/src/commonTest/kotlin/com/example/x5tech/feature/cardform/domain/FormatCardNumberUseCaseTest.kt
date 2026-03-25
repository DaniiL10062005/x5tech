package com.example.x5tech.feature.cardform.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class FormatCardNumberUseCaseTest {

    private val formatCardNumberUseCase = FormatCardNumberUseCase()

    @Test
    fun `should return empty string for empty card number input`() {
        val result = formatCardNumberUseCase("")

        assertEquals("", result)
    }

    @Test
    fun `should format short card number without trailing spaces`() {
        val result = formatCardNumberUseCase("123456")

        assertEquals("1234 56", result)
    }

    @Test
    fun `should remove invalid symbols from card number`() {
        val result = formatCardNumberUseCase("12ab34 cd56")

        assertEquals("1234 56", result)
    }

    @Test
    fun `should trim card number to 16 digits`() {
        val result = formatCardNumberUseCase("12345678901234567890")

        assertEquals("1234 5678 9012 3456", result)
    }

    @Test
    fun `should format card number with spaces`() {
        val result = formatCardNumberUseCase("1234567890123456")

        assertEquals("1234 5678 9012 3456", result)
    }
}
