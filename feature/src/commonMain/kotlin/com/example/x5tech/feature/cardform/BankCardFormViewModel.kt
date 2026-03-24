package com.example.x5tech.feature.cardform

import com.example.x5tech.model.domain.BankCard
import com.example.x5tech.model.repository.CardRepository
import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationResult
import com.example.x5tech.feature.cardform.domain.DetectBankByBinUseCase
import com.example.x5tech.feature.cardform.domain.DetectCardTypeUseCase
import com.example.x5tech.feature.cardform.domain.FormatCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.MaskCardDataUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardHolderNameUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCvvUseCase
import com.example.x5tech.feature.cardform.domain.ValidateExpiryDateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankCardFormViewModel(
    private val formatCardNumberUseCase: FormatCardNumberUseCase,
    private val validateCardNumberUseCase: ValidateCardNumberUseCase,
    private val validateExpiryDateUseCase: ValidateExpiryDateUseCase,
    private val validateCardHolderNameUseCase: ValidateCardHolderNameUseCase,
    private val validateCvvUseCase: ValidateCvvUseCase,
    private val detectBankByBinUseCase: DetectBankByBinUseCase,
    private val detectCardTypeUseCase: DetectCardTypeUseCase,
    private val maskCardDataUseCase: MaskCardDataUseCase,
    private val cardRepository: CardRepository,
    coroutineScope: CoroutineScope? = null,
) : KmpViewModel(coroutineScope = coroutineScope) {

    private val mutableState = MutableStateFlow(BankCardFormState())
    val state: StateFlow<BankCardFormState> = mutableState.asStateFlow()

    private var rawCardNumber: String = ""
    private var rawHolderName: String = ""
    private var rawExpiryDate: String = ""
    private var rawCvv: String = ""

    fun onCardNumberChanged(input: String) {
        rawCardNumber = input.filter(Char::isDigit).take(CARD_NUMBER_LENGTH)
        updateState()
    }

    fun onHolderNameChanged(input: String) {
        rawHolderName = formatHolderName(input)
        updateState()
    }

    fun onExpiryDateChanged(input: String) {
        rawExpiryDate = formatExpiryDate(input)
        updateState()
    }

    fun onCvvChanged(input: String) {
        rawCvv = input.filter(Char::isDigit).take(CVV_LENGTH)
        updateState()
    }

    fun onMaskToggled() {
        mutableState.update { currentState ->
            currentState.copy(isMasked = !currentState.isMasked)
        }
        updateState()
    }

    fun onSaveClicked() {
        updateState()
        val currentState = state.value
        if (!currentState.isSaveEnabled || currentState.isSaving) {
            return
        }

        viewModelScope.launch {
            mutableState.update {
                it.copy(
                    isSaving = true,
                    isSaved = false,
                    errorMessage = null,
                    isSaveEnabled = false,
                )
            }

            runCatching {
                cardRepository.saveCard(
                    card = BankCard(
                        number = rawCardNumber,
                        holderName = rawHolderName,
                        expirationMonth = rawExpiryDate.take(MONTH_LENGTH),
                        expirationYear = rawExpiryDate.takeLast(YEAR_LENGTH),
                        cvv = rawCvv,
                        type = detectCardTypeUseCase(rawCardNumber),
                    ),
                )
            }.onSuccess {
                mutableState.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        errorMessage = null,
                        isSaveEnabled = true,
                    )
                }
            }.onFailure { throwable ->
                mutableState.update {
                    it.copy(
                        isSaving = false,
                        isSaved = false,
                        errorMessage = throwable.message ?: DEFAULT_SAVE_ERROR_MESSAGE,
                        isSaveEnabled = canSave(),
                    )
                }
            }
        }
    }

    private fun updateState() {
        val cardNumberValidationResult = validateCardNumberUseCase(rawCardNumber)
        val holderNameValidationResult = validateCardHolderNameUseCase(rawHolderName)
        val expiryDateValidationResult = validateExpiryDateUseCase(rawExpiryDate)
        val cvvValidationResult = validateCvvUseCase(rawCvv)
        val bankName = detectBankByBinUseCase(rawCardNumber)
        val cardType = detectCardTypeUseCase(rawCardNumber)

        mutableState.update { currentState ->
            currentState.copy(
                cardNumber = if (currentState.isMasked) {
                    maskCardDataUseCase.maskCardNumber(rawCardNumber)
                } else {
                    formatCardNumberUseCase(rawCardNumber)
                },
                holderName = if (currentState.isMasked) {
                    maskCardDataUseCase.maskCardHolderName(rawHolderName)
                } else {
                    rawHolderName
                },
                expiryDate = rawExpiryDate,
                cvv = rawCvv,
                cardType = cardType,
                bankName = bankName,
                cardNumberValidationResult = cardNumberValidationResult,
                holderNameValidationResult = holderNameValidationResult,
                expiryDateValidationResult = expiryDateValidationResult,
                cvvValidationResult = cvvValidationResult,
                isSaveEnabled = !currentState.isSaving && areAllFieldsValid(
                    cardNumberValidationResult = cardNumberValidationResult,
                    holderNameValidationResult = holderNameValidationResult,
                    expiryDateValidationResult = expiryDateValidationResult,
                    cvvValidationResult = cvvValidationResult,
                ),
                isSaved = false,
                errorMessage = null,
            )
        }
    }

    private fun areAllFieldsValid(
        cardNumberValidationResult: CardValidationResult,
        holderNameValidationResult: CardValidationResult,
        expiryDateValidationResult: CardValidationResult,
        cvvValidationResult: CardValidationResult,
    ): Boolean {
        return cardNumberValidationResult is CardValidationResult.Valid &&
            holderNameValidationResult is CardValidationResult.Valid &&
            expiryDateValidationResult is CardValidationResult.Valid &&
            cvvValidationResult is CardValidationResult.Valid
    }

    private fun canSave(): Boolean {
        return areAllFieldsValid(
            cardNumberValidationResult = validateCardNumberUseCase(rawCardNumber),
            holderNameValidationResult = validateCardHolderNameUseCase(rawHolderName),
            expiryDateValidationResult = validateExpiryDateUseCase(rawExpiryDate),
            cvvValidationResult = validateCvvUseCase(rawCvv),
        )
    }

    private fun formatHolderName(input: String): String {
        return input
            .uppercase()
            .replace(MULTIPLE_SPACES_REGEX, SINGLE_SPACE)
            .trimStart()
    }

    private fun formatExpiryDate(input: String): String {
        val digits = input.filter(Char::isDigit).take(EXPIRY_DATE_DIGITS_LENGTH)

        return when {
            digits.length <= MONTH_LENGTH -> digits
            else -> "${digits.take(MONTH_LENGTH)}/${
                digits.drop(MONTH_LENGTH)
            }"
        }
    }

    private companion object {
        const val CARD_NUMBER_LENGTH = 16
        const val CVV_LENGTH = 3
        const val MONTH_LENGTH = 2
        const val YEAR_LENGTH = 2
        const val EXPIRY_DATE_DIGITS_LENGTH = 4
        const val SINGLE_SPACE = " "
        const val DEFAULT_SAVE_ERROR_MESSAGE = "Failed to save card"

        val MULTIPLE_SPACES_REGEX = Regex("\\s+")
    }
}


