package com.example.x5tech.feature.cardform.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MaskCardDataUseCaseTest {

    private val maskCardDataUseCase = MaskCardDataUseCase()

    @Test
    fun `masks card number showing first 6 and last 4 digits`() {
        val result = maskCardDataUseCase.maskCardNumber("4111111111111111")

        assertEquals("4111 11•• •••• 1111", result)
    }

    @Test
    fun `masks card number after removing non digit symbols`() {
        val result = maskCardDataUseCase.maskCardNumber("4111 1111-1111 1111")

        assertEquals("4111 11•• •••• 1111", result)
    }

    @Test
    fun `returns formatted number without mask when not enough digits to hide`() {
        val result = maskCardDataUseCase.maskCardNumber("1234567890")

        assertEquals("1234 5678 90", result)
    }

    @Test
    fun `masks card holder name partially`() {
        val result = maskCardDataUseCase.maskCardHolderName("JOHN SMITH")

        assertEquals("J••N S•••H", result)
    }

    @Test
    fun `normalizes lowercase name with extra spaces before masking`() {
        val result = maskCardDataUseCase.maskCardHolderName("  john   smith  ")

        assertEquals("J••N S•••H", result)
    }

    @Test
    fun `masks short name part conservatively`() {
        val result = maskCardDataUseCase.maskCardHolderName("AL")

        assertEquals("A•", result)
    }

    @Test
    fun `always returns fixed masked cvv`() {
        val result = maskCardDataUseCase.maskCvv("123")

        assertEquals("•••", result)
    }
}
