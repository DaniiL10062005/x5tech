package com.example.x5tech.model.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LocalBankByBinResolverTest {

    @Test
    fun `should resolve bank by first six digits`() {
        assertEquals("SberBank", LocalBankByBinResolver.resolve("54696700"))
    }

    @Test
    fun `should return null for unknown bin`() {
        assertNull(LocalBankByBinResolver.resolve("123456"))
    }
}
