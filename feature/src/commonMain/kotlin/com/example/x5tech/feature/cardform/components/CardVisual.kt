package com.example.x5tech.feature.cardform.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.x5tech.feature.cardform.resources.Res
import com.example.x5tech.feature.cardform.resources.*
import com.example.x5tech.model.domain.CardType
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CardVisual(
    cardType: CardType,
    bankName: String?,
    cardNumber: String,
    holderName: String,
    expiryDate: String,
    @Suppress("UNUSED_PARAMETER") cvv: String,
    isMasked: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(30.dp),
                ambientColor = Color(0x33111A25),
                spotColor = Color(0x33111A25),
            )
            .clip(RoundedCornerShape(30.dp))
            .background(cardBackgroundBrush(cardType))
            .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        CardDecorations()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    AnimatedCardText(
                        text = bankName ?: stringResource(Res.string.card_visual_bank_name_fallback),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = if (isMasked) {
                            stringResource(Res.string.card_visual_protected)
                        } else {
                            stringResource(Res.string.card_visual_live_preview)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.76f),
                    )
                }

                CardBrandMark(cardType = cardType)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.card_visual_number_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.62f),
                )
                AnimatedCardText(
                    text = displayValue(
                        value = cardNumber,
                        fallback = stringResource(Res.string.card_visual_number_fallback),
                    ),
                    style = MaterialTheme.typography.headlineMedium.copy(letterSpacing = 1.4.sp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                CardMetaBlock(
                    label = stringResource(Res.string.card_visual_holder_label),
                    value = displayValue(
                        value = holderName,
                        fallback = stringResource(Res.string.card_visual_holder_fallback),
                    ),
                    modifier = Modifier.weight(1.2f),
                )
                CardMetaBlock(
                    label = stringResource(Res.string.card_visual_expires_label),
                    value = displayValue(
                        value = expiryDate,
                        fallback = stringResource(Res.string.card_form_input_expiry_date_placeholder),
                    ),
                    modifier = Modifier.weight(0.7f),
                )
                CardMetaBlock(
                    label = stringResource(Res.string.card_visual_cvv_label),
                    value = stringResource(Res.string.card_form_input_cvv_placeholder),
                    modifier = Modifier.weight(0.45f),
                )
            }
        }
    }
}

@Composable
private fun BoxScope.CardDecorations() {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(132.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.22f),
                        Color.Transparent,
                    ),
                ),
            ),
    )
}

@Composable
private fun CardMetaBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.58f),
        )
        AnimatedCardText(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AnimatedCardText(
    text: String,
    style: TextStyle,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "card_text_animation",
    ) { targetText ->
        Text(
            text = targetText,
            style = style,
            fontWeight = fontWeight,
            color = Color.White,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}

@Composable
private fun CardBrandMark(cardType: CardType) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.72f)),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = cardTypeLabel(cardType),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = brandTint(cardType),
        )
    }
}

private fun displayValue(
    value: String,
    fallback: String,
): String {
    return value.ifBlank { fallback }
}

private fun brandTint(cardType: CardType): Color {
    return when (cardType) {
        CardType.VISA -> Color(0xFFA9DAFF)
        CardType.MASTERCARD -> Color(0xFFFFC17E)
        CardType.MIR -> Color(0xFF9BE7BA)
        CardType.UNKNOWN -> Color(0xFFDCE3EB)
    }
}

@Composable
private fun cardTypeLabel(cardType: CardType): String {
    return when (cardType) {
        CardType.VISA -> stringResource(Res.string.card_visual_brand_visa)
        CardType.MASTERCARD -> stringResource(Res.string.card_visual_brand_mastercard)
        CardType.MIR -> stringResource(Res.string.card_visual_brand_mir)
        CardType.UNKNOWN -> stringResource(Res.string.card_visual_brand_auto)
    }
}

private fun cardBackgroundBrush(cardType: CardType): Brush {
    return when (cardType) {
        CardType.VISA -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF0B1730),
                Color(0xFF163B74),
                Color(0xFF33A0C8),
            ),
        )
        CardType.MASTERCARD -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF22120E),
                Color(0xFF6B2D14),
                Color(0xFFC96C2A),
            ),
        )
        CardType.MIR -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF0E281F),
                Color(0xFF175943),
                Color(0xFF2CA86E),
            ),
        )
        CardType.UNKNOWN -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF161E2A),
                Color(0xFF344356),
                Color(0xFF667487),
            ),
        )
    }
}
