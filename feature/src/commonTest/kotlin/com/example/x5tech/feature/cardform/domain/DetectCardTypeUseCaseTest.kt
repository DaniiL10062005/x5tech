package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DetectCardTypeUseCaseTest {

    private val detectCardTypeUseCase = DetectCardTypeUseCase()

    @Test
    fun `returns visa for card number starting with 4`() {
        val result = detectCardTypeUseCase("4111 1111 1111 1111")

        assertEquals(CardType.VISA, result)
    }

    @Test
    fun `returns mastercard for range 51 to 55`() {
        val result = detectCardTypeUseCase("5555 5555 5555 4444")

        assertEquals(CardType.MASTERCARD, result)
    }

    @Test
    fun `returns mastercard for range 2221 to 2720`() {
        val result = detectCardTypeUseCase("2221 0000 0000 0009")

        assertEquals(CardType.MASTERCARD, result)
    }

    @Test
    fun `returns mir for range 2200 to 2204`() {
        val result = detectCardTypeUseCase("2200 0000 0000 0004")

        assertEquals(CardType.MIR, result)
    }

    @Test
    fun `returns unknown for unsupported prefix`() {
        val result = detectCardTypeUseCase("9999 0000 0000 0000")

        assertEquals(CardType.UNKNOWN, result)
    }

    @Test
    fun `returns unknown when input does not contain enough digits for known ranges`() {
        val result = detectCardTypeUseCase("2")

        assertEquals(CardType.UNKNOWN, result)
    }
}
