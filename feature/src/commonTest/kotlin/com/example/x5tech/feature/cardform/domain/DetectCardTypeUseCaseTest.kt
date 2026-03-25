package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DetectCardTypeUseCaseTest {

    private val detectCardTypeUseCase = DetectCardTypeUseCase()

    @Test
    fun `should detect VISA card type`() {
        val result = detectCardTypeUseCase("4111 1111 1111 1111")

        assertEquals(CardType.VISA, result)
    }

    @Test
    fun `should detect MASTERCARD card type`() {
        val result = detectCardTypeUseCase("5555 5555 5555 4444")

        assertEquals(CardType.MASTERCARD, result)
    }

    @Test
    fun `should detect MASTERCARD card type for 2221 to 2720 range`() {
        val result = detectCardTypeUseCase("2221 0000 0000 0009")

        assertEquals(CardType.MASTERCARD, result)
    }

    @Test
    fun `should detect MIR card type`() {
        val result = detectCardTypeUseCase("2200 0000 0000 0004")

        assertEquals(CardType.MIR, result)
    }

    @Test
    fun `should return UNKNOWN for unsupported prefix`() {
        val result = detectCardTypeUseCase("9999 0000 0000 0000")

        assertEquals(CardType.UNKNOWN, result)
    }

    @Test
    fun `should return UNKNOWN when input does not contain enough digits`() {
        val result = detectCardTypeUseCase("2")

        assertEquals(CardType.UNKNOWN, result)
    }
}
