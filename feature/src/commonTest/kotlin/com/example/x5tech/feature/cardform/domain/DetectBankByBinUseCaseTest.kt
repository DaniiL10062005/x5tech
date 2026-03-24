package com.example.x5tech.feature.cardform.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class DetectBankByBinUseCaseTest {

    private val detectBankByBinUseCase = DetectBankByBinUseCase()

    @Test
    fun `should detect SberBank by bin range`() {
        val result = detectBankByBinUseCase("5469670026596588")

        assertEquals("SberBank", result)
    }

    @Test
    fun `should detect T-Bank by bin range`() {
        val result = detectBankByBinUseCase("5213241234567890")

        assertEquals("T-Bank", result)
    }

    @Test
    fun `should detect Alfa-Bank by bin range`() {
        val result = detectBankByBinUseCase("4584431234567890")

        assertEquals("Alfa-Bank", result)
    }

    @Test
    fun `should detect VTB by bin range`() {
        val result = detectBankByBinUseCase("2200241234567890")

        assertEquals("VTB", result)
    }

    @Test
    fun `should detect Gazprombank by bin range`() {
        val result = detectBankByBinUseCase("5489991234567890")

        assertEquals("Gazprombank", result)
    }

    @Test
    fun `should return null for unsupported bin`() {
        val result = detectBankByBinUseCase("9999991234567890")

        assertNull(result)
    }

    @Test
    fun `should return null when input has less than six digits`() {
        val result = detectBankByBinUseCase("2200")

        assertNull(result)
    }
}
