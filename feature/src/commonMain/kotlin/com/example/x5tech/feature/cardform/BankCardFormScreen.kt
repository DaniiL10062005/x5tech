package com.example.x5tech.feature.cardform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.x5tech.feature.cardform.components.CardInputField
import com.example.x5tech.feature.cardform.components.CardVisual
import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult

@Composable
fun BankCardFormScreen(
    viewModel: BankCardFormViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    BankCardFormScreen(
        state = state,
        onCardNumberChanged = viewModel::onCardNumberChanged,
        onHolderNameChanged = viewModel::onHolderNameChanged,
        onExpiryDateChanged = viewModel::onExpiryDateChanged,
        onCvvChanged = viewModel::onCvvChanged,
        onMaskToggled = viewModel::onMaskToggled,
        onSaveClicked = viewModel::onSaveClicked,
        modifier = modifier,
    )
}

@Composable
internal fun BankCardFormScreen(
    state: BankCardFormState,
    onCardNumberChanged: (String) -> Unit,
    onHolderNameChanged: (String) -> Unit,
    onExpiryDateChanged: (String) -> Unit,
    onCvvChanged: (String) -> Unit,
    onMaskToggled: () -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7F3EC),
                        Color(0xFFE7EEF5),
                        Color(0xFFF7F3EC),
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HeaderBlock(
                cardType = state.cardType,
                bankName = state.bankName,
            )

            CardVisual(
                cardType = state.cardType,
                bankName = state.bankName,
                cardNumber = state.cardNumber,
                holderName = state.holderName,
                expiryDate = state.expiryDate,
                cvv = state.cvv,
                isMasked = state.isMasked,
            )

            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                shadowElevation = 10.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "Card details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Enter the card information below. The form validates input as you type.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    CardInputField(
                        label = "Card number",
                        value = state.cardNumber,
                        onValueChange = onCardNumberChanged,
                        isError = state.cardNumberValidationResult is CardValidationResult.Invalid,
                        errorText = state.cardNumberValidationResult.toErrorText(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )

                    CardInputField(
                        label = "Cardholder name",
                        value = state.holderName,
                        onValueChange = onHolderNameChanged,
                        isError = state.holderNameValidationResult is CardValidationResult.Invalid,
                        errorText = state.holderNameValidationResult.toErrorText(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CardInputField(
                            label = "Expiry date",
                            value = state.expiryDate,
                            onValueChange = onExpiryDateChanged,
                            isError = state.expiryDateValidationResult is CardValidationResult.Invalid,
                            errorText = state.expiryDateValidationResult.toErrorText(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                        )
                        CardInputField(
                            label = "CVV",
                            value = state.cvv,
                            onValueChange = onCvvChanged,
                            isError = state.cvvValidationResult is CardValidationResult.Invalid,
                            errorText = state.cvvValidationResult.toErrorText(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                            ),
                            modifier = Modifier.weight(0.75f),
                            visualTransformation = PasswordVisualTransformation(),
                        )
                    }

                    ShowHideSwitch(
                        isMasked = state.isMasked,
                        onMaskToggled = onMaskToggled,
                    )

                    state.errorMessage?.let { message ->
                        StatusBadge(
                            text = message,
                            containerColor = Color(0xFFFBEAE8),
                            contentColor = Color(0xFFB42318),
                        )
                    }

                    if (state.isSaved) {
                        StatusBadge(
                            text = "Card saved successfully",
                            containerColor = Color(0xFFE8F5EC),
                            contentColor = Color(0xFF1F6A43),
                        )
                    }

                    Button(
                        onClick = onSaveClicked,
                        enabled = state.isSaveEnabled && !state.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        } else {
                            Text(
                                text = "Save card",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderBlock(
    cardType: CardType,
    bankName: String?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Bank card form",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = buildString {
                append(cardTypeTitle(cardType))
                bankName?.let {
                    append(" · ")
                    append(it)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ShowHideSwitch(
    isMasked: Boolean,
    onMaskToggled: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "Hide sensitive data",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Masks card number and cardholder name in the preview.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Switch(
                checked = isMasked,
                onCheckedChange = { onMaskToggled() },
            )
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
        )
    }
}

private fun CardValidationResult?.toErrorText(): String? {
    val invalidResult = this as? CardValidationResult.Invalid ?: return null
    val error = invalidResult.errors.firstOrNull() ?: return null

    return when (error) {
        CardValidationError.EMPTY_NUMBER -> "Enter the card number"
        CardValidationError.INVALID_NUMBER -> "Enter a valid card number"
        CardValidationError.EMPTY_HOLDER_NAME -> "Enter the cardholder name"
        CardValidationError.INVALID_HOLDER_NAME -> "Use only uppercase Latin letters and spaces"
        CardValidationError.EMPTY_EXPIRATION_DATE -> "Enter the expiry date"
        CardValidationError.INVALID_EXPIRATION_DATE -> "Enter a valid expiry date in MM/YY"
        CardValidationError.EMPTY_CVV -> "Enter the CVV"
        CardValidationError.INVALID_CVV -> "CVV must contain exactly 3 digits"
    }
}

private fun cardTypeTitle(cardType: CardType): String {
    return when (cardType) {
        CardType.VISA -> "Visa"
        CardType.MASTERCARD -> "Mastercard"
        CardType.MIR -> "Mir"
        CardType.UNKNOWN -> "Card type will be detected automatically"
    }
}
