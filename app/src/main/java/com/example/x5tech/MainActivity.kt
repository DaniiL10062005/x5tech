package com.example.x5tech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.x5tech.feature.cardform.BankCardFormScreen
import com.example.x5tech.feature.cardform.BankCardFormViewModel
import com.example.x5tech.ui.theme.X5techTheme
import org.koin.core.context.GlobalContext

internal class MainActivity : ComponentActivity() {

    private val viewModel: BankCardFormViewModel by lazy {
        GlobalContext.get().get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            X5techTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    BankCardFormScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
