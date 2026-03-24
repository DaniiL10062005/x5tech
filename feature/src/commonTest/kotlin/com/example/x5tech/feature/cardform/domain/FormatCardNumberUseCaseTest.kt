package com.example.x5tech.feature.cardform.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class FormatCardNumberUseCaseTest {

    private val formatCardNumberUseCase = FormatCardNumberUseCase()

    @Test
    fun `returns empty string for empty input`() {
        val result = formatCardNumberUseCase("")

        assertEquals("", result)
    }

    @Test
    fun `formats short card number without trailing spaces`() {
        val result = formatCardNumberUseCase("123456")

        assertEquals("1234 56", result)
    }

    @Test
    fun `removes non digit characters including letters`() {
        val result = formatCardNumberUseCase("12ab34 cd56")

        assertEquals("1234 56", result)
    }

    @Test
    fun `limits card number to 16 digits before formatting`() {
        val result = formatCardNumberUseCase("12345678901234567890")

        assertEquals("1234 5678 9012 3456", result)
    }

    @Test
    fun `formats full 16 digit card number into groups of four`() {
        val result = formatCardNumberUseCase("1234567890123456")

        assertEquals("1234 5678 9012 3456", result)
    }
}
