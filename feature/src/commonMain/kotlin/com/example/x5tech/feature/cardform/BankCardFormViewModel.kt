package com.example.x5tech.feature.cardform

import com.example.x5tech.model.domain.BankCard
import com.example.x5tech.model.repository.CardRepository
import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationResult
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BankCardFormViewModel internal constructor(
    private val formatCardNumberUseCase: FormatCardNumberUseCase,
    private val validateCardNumberUseCase: ValidateCardNumberUseCase,
    private val validateExpiryDateUseCase: ValidateExpiryDateUseCase,
    private val validateCardHolderNameUseCase: ValidateCardHolderNameUseCase,
    private val validateCvvUseCase: ValidateCvvUseCase,
    private val detectCardTypeUseCase: DetectCardTypeUseCase,
    private val maskCardDataUseCase: MaskCardDataUseCase,
    private val cardRepository: CardRepository,
    coroutineScope: CoroutineScope? = null,
) : KmpViewModel(coroutineScope = coroutineScope) {

    private val mutableState = MutableStateFlow(BankCardFormState())
    internal val state: StateFlow<BankCardFormState> = mutableState.asStateFlow()

    private var rawCardNumber: String = ""
    private var rawHolderName: String = ""
    private var rawExpiryDate: String = ""
    private var rawCvv: String = ""
    private var resolvedBankName: String? = null
    private var requestedBin: String? = null
    private var bankLookupJob: Job? = null

    internal fun onCardNumberChanged(input: String) {
        rawCardNumber = input.filter(Char::isDigit).take(CARD_NUMBER_LENGTH)
        mutableState.update { currentState ->
            currentState.copy(isCardNumberTouched = true)
        }
        lookupBankByBin()
        updateState()
    }

    internal fun onHolderNameChanged(input: String) {
        rawHolderName = formatHolderName(input)
        mutableState.update { currentState ->
            currentState.copy(isHolderNameTouched = true)
        }
        updateState()
    }

    internal fun onExpiryDateChanged(input: String) {
        rawExpiryDate = formatExpiryDate(input)
        mutableState.update { currentState ->
            currentState.copy(isExpiryDateTouched = true)
        }
        updateState()
    }

    internal fun onCvvChanged(input: String) {
        rawCvv = input.filter(Char::isDigit).take(CVV_LENGTH)
        mutableState.update { currentState ->
            currentState.copy(isCvvTouched = true)
        }
        updateState()
    }

    internal fun onMaskToggled() {
        mutableState.update { currentState ->
            currentState.copy(isMasked = !currentState.isMasked)
        }
        updateState()
    }

    internal fun onSaveClicked() {
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
                    isCardNumberTouched = true,
                    isHolderNameTouched = true,
                    isExpiryDateTouched = true,
                    isCvvTouched = true,
                )
            }

            runCatching {
                cardRepository.saveCard(
                    card = BankCard(
                        number = rawCardNumber,
                        holderName = rawHolderName,
                        expiryDate = rawExpiryDate,
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
                        errorMessage = throwable.toUiText(),
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
                bankName = resolvedBankName,
                isCardNumberTouched = currentState.isCardNumberTouched,
                isHolderNameTouched = currentState.isHolderNameTouched,
                isExpiryDateTouched = currentState.isExpiryDateTouched,
                isCvvTouched = currentState.isCvvTouched,
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

    private fun lookupBankByBin() {
        val bin = currentBin()
        if (bin == null) {
            bankLookupJob?.cancel()
            bankLookupJob = null
            requestedBin = null
            resolvedBankName = null
            return
        }

        if (bin == requestedBin) {
            return
        }

        requestedBin = bin
        resolvedBankName = null
        bankLookupJob?.cancel()
        bankLookupJob = viewModelScope.launch {
            val bankName = runCatching {
                cardRepository.getBankByBin(bin)
            }.getOrNull()

            if (currentBin() == bin) {
                resolvedBankName = bankName
                mutableState.update { currentState ->
                    currentState.copy(bankName = bankName)
                }
            }
        }
    }

    private fun currentBin(): String? {
        if (rawCardNumber.length < MIN_BIN_LENGTH) {
            return null
        }

        return rawCardNumber.take(MAX_BIN_LENGTH)
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
        const val EXPIRY_DATE_DIGITS_LENGTH = 4
        const val MIN_BIN_LENGTH = 6
        const val MAX_BIN_LENGTH = 8
        const val SINGLE_SPACE = " "

        val MULTIPLE_SPACES_REGEX = Regex("\\s+")
    }
}


