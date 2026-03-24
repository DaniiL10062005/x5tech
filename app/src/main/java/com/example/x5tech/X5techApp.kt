package com.example.x5tech

import android.app.Application
import com.example.x5tech.feature.cardform.createBankCardFormModule
import com.example.x5tech.feature.cardform.domain.CurrentDate
import com.example.x5tech.feature.cardform.domain.CurrentDateProvider
import org.koin.core.context.startKoin
import java.util.Calendar

class X5techApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                createBankCardFormModule(
                    currentDateProvider = AndroidCurrentDateProvider(),
                ),
            )
        }
    }
}

private class AndroidCurrentDateProvider : CurrentDateProvider {

    override fun getCurrentDate(): CurrentDate {
        val calendar = Calendar.getInstance()

        return CurrentDate(
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR) % 100,
        )
    }
}
