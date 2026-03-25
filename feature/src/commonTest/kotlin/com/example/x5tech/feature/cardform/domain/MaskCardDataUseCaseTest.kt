package com.example.x5tech.feature.cardform.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MaskCardDataUseCaseTest {

    private val maskCardDataUseCase = MaskCardDataUseCase()

    @Test
    fun `should mask card number correctly`() {
        val result = maskCardDataUseCase.maskCardNumber("4111111111111111")

        assertEquals("4111 11•• •••• 1111", result)
    }

    @Test
    fun `should mask card number after removing non digit symbols`() {
        val result = maskCardDataUseCase.maskCardNumber("4111 1111-1111 1111")

        assertEquals("4111 11•• •••• 1111", result)
    }

    @Test
    fun `should keep short card number visible when there are not enough digits to hide`() {
        val result = maskCardDataUseCase.maskCardNumber("1234567890")

        assertEquals("1234 5678 90", result)
    }

    @Test
    fun `should mask card holder name partially`() {
        val result = maskCardDataUseCase.maskCardHolderName("JOHN SMITH")

        assertEquals("J••N S•••H", result)
    }

    @Test
    fun `should normalize lowercase card holder name before masking`() {
        val result = maskCardDataUseCase.maskCardHolderName("  john   smith  ")

        assertEquals("J••N S•••H", result)
    }

    @Test
    fun `should mask short card holder name conservatively`() {
        val result = maskCardDataUseCase.maskCardHolderName("AL")

        assertEquals("A•", result)
    }

    @Test
    fun `should always return fixed masked CVV`() {
        val result = maskCardDataUseCase.maskCvv("123")

        assertEquals("•••", result)
    }
}
