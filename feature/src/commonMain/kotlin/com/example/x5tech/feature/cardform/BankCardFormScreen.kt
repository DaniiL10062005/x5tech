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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.x5tech.feature.cardform.resources.Res
import com.example.x5tech.feature.cardform.resources.*
import com.example.x5tech.model.domain.CardType
import com.example.x5tech.model.domain.CardValidationError
import com.example.x5tech.model.domain.CardValidationResult
import org.jetbrains.compose.resources.stringResource

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
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF9F1E6),
                        Color(0xFFE9EEF7),
                        Color(0xFFF7F4EF),
                    ),
                ),
            ),
    ) {
        AmbientBackdrop()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
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
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                tonalElevation = 4.dp,
                shadowElevation = 16.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    FormHeader()

                    CardInputField(
                        label = stringResource(Res.string.card_form_input_card_number),
                        value = state.cardNumber,
                        onValueChange = onCardNumberChanged,
                        isError = state.cardNumberValidationResult is CardValidationResult.Invalid,
                        errorText = state.cardNumberValidationResult.toErrorText(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = stringResource(Res.string.card_form_input_card_number_placeholder),
                    )

                    CardInputField(
                        label = stringResource(Res.string.card_form_input_holder_name),
                        value = state.holderName,
                        onValueChange = onHolderNameChanged,
                        isError = state.holderNameValidationResult is CardValidationResult.Invalid,
                        errorText = state.holderNameValidationResult.toErrorText(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = stringResource(Res.string.card_form_input_holder_name_placeholder),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CardInputField(
                            label = stringResource(Res.string.card_form_input_expiry_date),
                            value = state.expiryDate,
                            onValueChange = onExpiryDateChanged,
                            isError = state.expiryDateValidationResult is CardValidationResult.Invalid,
                            errorText = state.expiryDateValidationResult.toErrorText(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            placeholder = stringResource(Res.string.card_form_input_expiry_date_placeholder),
                        )
                        CardInputField(
                            label = stringResource(Res.string.card_form_input_cvv),
                            value = state.cvv,
                            onValueChange = onCvvChanged,
                            isError = state.cvvValidationResult is CardValidationResult.Invalid,
                            errorText = state.cvvValidationResult.toErrorText(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                            ),
                            modifier = Modifier.weight(0.75f),
                            visualTransformation = PasswordVisualTransformation(),
                            placeholder = stringResource(Res.string.card_form_input_cvv_placeholder),
                        )
                    }

                    ShowHideSwitch(
                        isMasked = state.isMasked,
                        onMaskToggled = onMaskToggled,
                    )

                    state.errorMessage?.let { message ->
                        StatusBadge(
                            text = message.asString(),
                            containerColor = Color(0xFFFBEAE8),
                            contentColor = Color(0xFFB42318),
                        )
                    }

                    if (state.isSaved) {
                        StatusBadge(
                            text = stringResource(Res.string.card_form_saved_success),
                            containerColor = Color(0xFFE8F5EC),
                            contentColor = Color(0xFF1F6A43),
                        )
                    }

                    Button(
                        onClick = onSaveClicked,
                        enabled = state.isSaveEnabled && !state.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF173A63),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFB8C4D1),
                            disabledContentColor = Color.White.copy(alpha = 0.78f),
                        ),
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color.White,
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.card_form_save_action),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AmbientBackdrop() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 64.dp, y = (-24).dp)
                .size(220.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x55A7D8EE),
                            Color.Transparent,
                        ),
                    ),
                    shape = CircleShape,
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-80).dp, y = 50.dp)
                .size(180.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x44F2BF8A),
                            Color.Transparent,
                        ),
                    ),
                    shape = CircleShape,
                ),
        )
    }
}

@Composable
private fun HeaderBlock(
    cardType: CardType,
    bankName: String?,
) {
    val cardTypeTitle = cardTypeTitle(cardType).asString()
    val bankSeparator = stringResource(Res.string.card_form_header_bank_separator)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(99.dp),
            color = Color.White.copy(alpha = 0.7f),
        ) {
            Text(
                text = stringResource(Res.string.card_form_label_wallet_setup),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3E566F),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }

        Text(
            text = stringResource(Res.string.card_form_header_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = buildString {
                append(cardTypeTitle)
                bankName?.let {
                    append(bankSeparator)
                    append(it)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FormHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(99.dp),
            color = Color(0xFFEAF0F6),
        ) {
            Text(
                text = stringResource(Res.string.card_form_label_secure_form),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF25486D),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }

        Text(
            text = stringResource(Res.string.card_form_form_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(Res.string.card_form_form_description),
            style = MaterialTheme.typography.bodyMedium,
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
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF2F6F9),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.card_form_mask_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(Res.string.card_form_mask_description),
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

@Composable
private fun CardValidationResult?.toErrorText(): String? {
    val invalidResult = this as? CardValidationResult.Invalid ?: return null
    val error = invalidResult.errors.firstOrNull() ?: return null

    return when (error) {
        CardValidationError.EMPTY_NUMBER ->
            stringResource(Res.string.card_form_error_empty_number)
        CardValidationError.INVALID_NUMBER ->
            stringResource(Res.string.card_form_error_invalid_number)
        CardValidationError.EMPTY_HOLDER_NAME ->
            stringResource(Res.string.card_form_error_empty_holder_name)
        CardValidationError.INVALID_HOLDER_NAME ->
            stringResource(Res.string.card_form_error_invalid_holder_name)
        CardValidationError.EMPTY_EXPIRATION_DATE ->
            stringResource(Res.string.card_form_error_empty_expiration_date)
        CardValidationError.INVALID_EXPIRATION_DATE ->
            stringResource(Res.string.card_form_error_invalid_expiration_date)
        CardValidationError.EMPTY_CVV ->
            stringResource(Res.string.card_form_error_empty_cvv)
        CardValidationError.INVALID_CVV ->
            stringResource(Res.string.card_form_error_invalid_cvv)
    }
}

private fun cardTypeTitle(cardType: CardType): UiText {
    return when (cardType) {
        CardType.VISA -> UiText.Resource(Res.string.card_form_card_type_visa_ready)
        CardType.MASTERCARD -> UiText.Resource(Res.string.card_form_card_type_mastercard_ready)
        CardType.MIR -> UiText.Resource(Res.string.card_form_card_type_mir_ready)
        CardType.UNKNOWN -> UiText.Resource(Res.string.card_form_card_type_unknown)
    }
}
