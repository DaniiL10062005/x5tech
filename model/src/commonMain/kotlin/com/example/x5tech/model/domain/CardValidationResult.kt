package com.example.x5tech.model.domain

sealed class CardValidationResult {

    data object Valid : CardValidationResult()

    data class Invalid(
        val errors: List<CardValidationError>,
    ) : CardValidationResult() {
        init {
            require(errors.isNotEmpty()) {
                "Invalid validation result must contain at least one error."
            }
        }
    }

    val isValid: Boolean
        get() = this is Valid
}

enum class CardValidationError {
    EMPTY_NUMBER,
    INVALID_NUMBER,
    EMPTY_HOLDER_NAME,
    INVALID_HOLDER_NAME,
    EMPTY_EXPIRATION_DATE,
    INVALID_EXPIRATION_DATE,
    EMPTY_CVV,
    INVALID_CVV,
}
