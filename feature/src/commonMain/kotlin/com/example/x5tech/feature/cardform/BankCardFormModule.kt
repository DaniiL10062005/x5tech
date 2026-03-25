package com.example.x5tech.feature.cardform

import com.example.x5tech.model.repository.CardRepository
import com.example.x5tech.model.repository.BinlistCardRepository
import com.example.x5tech.feature.cardform.domain.CurrentDateProvider
import com.example.x5tech.feature.cardform.domain.DetectCardTypeUseCase
import com.example.x5tech.feature.cardform.domain.FormatCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.MaskCardDataUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardHolderNameUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCardNumberUseCase
import com.example.x5tech.feature.cardform.domain.ValidateCvvUseCase
import com.example.x5tech.feature.cardform.domain.ValidateExpiryDateUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun createBankCardFormModule(
    currentDateProvider: CurrentDateProvider,
): Module = module {
    single<CurrentDateProvider> { currentDateProvider }

    factory { FormatCardNumberUseCase() }
    factory { ValidateCardNumberUseCase() }
    factory { ValidateExpiryDateUseCase(currentDateProvider = get()) }
    factory { ValidateCardHolderNameUseCase() }
    factory { ValidateCvvUseCase() }
    factory { DetectCardTypeUseCase() }
    factory { MaskCardDataUseCase() }

    single<CardRepository> {
        BinlistCardRepository()
    }

    factory {
        BankCardFormViewModel(
            formatCardNumberUseCase = get(),
            validateCardNumberUseCase = get(),
            validateExpiryDateUseCase = get(),
            validateCardHolderNameUseCase = get(),
            validateCvvUseCase = get(),
            detectCardTypeUseCase = get(),
            maskCardDataUseCase = get(),
            cardRepository = get(),
        )
    }
}


