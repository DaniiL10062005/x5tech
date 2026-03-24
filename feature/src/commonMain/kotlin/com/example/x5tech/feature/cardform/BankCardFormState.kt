package com.example.x5tech.feature.cardform

import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationResult

data class BankCardFormState(
    val cardNumber: String = "",
    val holderName: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val cardType: CardType = CardType.UNKNOWN,
    val bankName: String? = null,
    val isMasked: Boolean = false,
    val cardNumberValidationResult: CardValidationResult? = null,
    val holderNameValidationResult: CardValidationResult? = null,
    val expiryDateValidationResult: CardValidationResult? = null,
    val cvvValidationResult: CardValidationResult? = null,
    val isSaveEnabled: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)

