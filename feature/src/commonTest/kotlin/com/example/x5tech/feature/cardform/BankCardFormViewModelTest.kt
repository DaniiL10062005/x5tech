package com.example.x5tech.feature.cardform

import com.example.x5tech.feature.cardform.domain.CurrentDate
import com.example.x5tech.feature.cardform.domain.CurrentDateProvider
import com.example.x5tech.feature.cardform.domain.DetectBankByBinUseCase
import com.example.x5tech.feature.cardform.domain.DetectCardTypeUseCase
import com.example.x5tech.feature.cardform.domain.FormatCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.MaskCardDataUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardHolderNameUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCvvUseCase
import com.example.x5tech.feature.cardform.domain.ValidateExpiryDateUseCase
import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import com.example.x5tech.model.repository.FakeCardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class BankCardFormViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `should enable save button only when all fields valid`() = runTest(testDispatcher) {
        val viewModel = createViewModel(coroutineScope = this)

        viewModel.onCardNumberChanged("4111111111111111")
        viewModel.onHolderNameChanged("JOHN SMITH")
        viewModel.onExpiryDateChanged("03/30")
        viewModel.onCvvChanged("12")

        assertFalse(viewModel.state.value.isSaveEnabled)

        viewModel.onCvvChanged("123")

        assertTrue(viewModel.state.value.isSaveEnabled)
    }

    @Test
    fun `should detect VISA card type`() = runTest(testDispatcher) {
        val viewModel = createViewModel(coroutineScope = this)

        viewModel.onCardNumberChanged("4111111111111111")

        assertEquals(CardType.VISA, viewModel.state.value.cardType)
    }

    @Test
    fun `should detect MASTERCARD card type`() = runTest(testDispatcher) {
        val viewModel = createViewModel(coroutineScope = this)

        viewModel.onCardNumberChanged("5555555555554444")

        assertEquals(CardType.MASTERCARD, viewModel.state.value.cardType)
    }

    @Test
    fun `should update validation state for invalid card number`() = runTest(testDispatcher) {
        val viewModel = createViewModel(coroutineScope = this)

        viewModel.onCardNumberChanged("1234")

        assertEquals(
            CardValidationResult.Invalid(
                errors = listOf(CardValidationError.INVALID_NUMBER),
            ),
            viewModel.state.value.cardNumberValidationResult,
        )
    }

    @Test
    fun `should toggle masked mode`() = runTest(testDispatcher) {
        val viewModel = createViewModel(coroutineScope = this)

        viewModel.onCardNumberChanged("4111111111111111")
        viewModel.onHolderNameChanged("JOHN SMITH")
        viewModel.onCvvChanged("123")

        assertFalse(viewModel.state.value.isMasked)

        viewModel.onMaskToggled()

        assertTrue(viewModel.state.value.isMasked)
        assertEquals("4111 11•• •••• 1111", viewModel.state.value.cardNumber)
        assertEquals("J••N S•••H", viewModel.state.value.holderName)
        assertEquals("123", viewModel.state.value.cvv)
    }

    @Test
    fun `should call repository on save when form valid`() = runTest(testDispatcher) {
        val repository = FakeCardRepository(delayMillis = 0)
        val viewModel = createViewModel(
            cardRepository = repository,
            coroutineScope = this,
        )

        viewModel.onCardNumberChanged("4111111111111111")
        viewModel.onHolderNameChanged("JOHN SMITH")
        viewModel.onExpiryDateChanged("03/30")
        viewModel.onCvvChanged("123")

        viewModel.onSaveClicked()
        advanceUntilIdle()

        assertEquals(1, repository.getSavedCards().size)
        assertTrue(viewModel.state.value.isSaved)
        assertFalse(viewModel.state.value.isSaving)
    }

    private fun createViewModel(
        coroutineScope: CoroutineScope,
        cardRepository: FakeCardRepository = FakeCardRepository(delayMillis = 0),
    ): BankCardFormViewModel {
        return BankCardFormViewModel(
            formatCardNumberUseCase = FormatCardNumberUseCase(),
            validateCardNumberUseCase = ValidateCardNumberUseCase(),
            validateExpiryDateUseCase = ValidateExpiryDateUseCase(
                currentDateProvider = FakeCurrentDateProvider(),
            ),
            validateCardHolderNameUseCase = ValidateCardHolderNameUseCase(),
            validateCvvUseCase = ValidateCvvUseCase(),
            detectBankByBinUseCase = DetectBankByBinUseCase(),
            detectCardTypeUseCase = DetectCardTypeUseCase(),
            maskCardDataUseCase = MaskCardDataUseCase(),
            cardRepository = cardRepository,
            coroutineScope = coroutineScope,
        )
    }
}

internal class FakeCurrentDateProvider : CurrentDateProvider {

    override fun getCurrentDate(): CurrentDate {
        return CurrentDate(
            month = 3,
            year = 26,
        )
    }
}
