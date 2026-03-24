package com.example.x5tech.feature.cardform.domain

import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidateExpiryDateUseCaseTest {

    private val currentDateProvider = FakeCurrentDateProvider(
        currentDate = CurrentDate(
            month = 3,
            year = 26,
        ),
    )

    private val validateExpiryDateUseCase = ValidateExpiryDateUseCase(currentDateProvider)

    @Test
    fun `returns valid for current month and year`() {
        val result = validateExpiryDateUseCase("03/26")

        assertEquals(CardValidationResult.Valid, result)
    }

    @Test
    fun `returns invalid for expired date`() {
        val result = validateExpiryDateUseCase("02/26")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_EXPIRATION_DATE),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for month outside 01 to 12`() {
        val result = validateExpiryDateUseCase("13/26")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_EXPIRATION_DATE),
            ),
            result,
        )
    }

    @Test
    fun `returns invalid for incorrect format`() {
        val result = validateExpiryDateUseCase("3/2026")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_EXPIRATION_DATE),
            ),
            result,
        )
    }
}

internal class FakeCurrentDateProvider(
    private val currentDate: CurrentDate,
) : CurrentDateProvider {

    override fun getCurrentDate(): CurrentDate = currentDate
}
