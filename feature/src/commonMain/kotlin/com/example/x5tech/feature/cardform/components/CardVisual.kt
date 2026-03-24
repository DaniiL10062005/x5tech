package com.example.x5tech.feature.cardform.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.x5tech.model.domain.CardType

@Composable
fun CardVisual(
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
            .clip(RoundedCornerShape(28.dp))
            .background(cardBackgroundBrush(cardType))
            .padding(22.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = bankName ?: "Bank Card",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.76f),
                    )
                    Text(
                        text = cardTypeLabel(cardType),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CardBrandMark(cardType = cardType)
                    Text(
                        text = if (isMasked) "Protected mode" else "Preview mode",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedCardText(
                text = displayValue(
                    value = cardNumber,
                    fallback = "0000 0000 0000 0000",
                ),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CardMetaBlock(
                    label = "Cardholder",
                    value = displayValue(
                        value = holderName,
                        fallback = "YOUR NAME",
                    ),
                    modifier = Modifier.weight(1.2f),
                )
                CardMetaBlock(
                    label = "Expires",
                    value = displayValue(
                        value = expiryDate,
                        fallback = "MM/YY",
                    ),
                    modifier = Modifier.weight(0.8f),
                )
                CardMetaBlock(
                    label = "CVV",
                    value = "•••",
                    modifier = Modifier.weight(0.55f),
                )
            }
        }
    }
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
            color = Color.White.copy(alpha = 0.68f),
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
        horizontalArrangement = Arrangement.spacedBy((-10).dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.82f)),
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    when (cardType) {
                        CardType.VISA -> Color(0xFF8FD3FF)
                        CardType.MASTERCARD -> Color(0xFFFFB66B)
                        CardType.MIR -> Color(0xFF8CE4B0)
                        CardType.UNKNOWN -> Color.White.copy(alpha = 0.38f)
                    },
                ),
        )
    }
}

private fun displayValue(
    value: String,
    fallback: String,
): String {
    return value.ifBlank { fallback }
}

private fun cardTypeLabel(cardType: CardType): String {
    return when (cardType) {
        CardType.VISA -> "VISA"
        CardType.MASTERCARD -> "MASTERCARD"
        CardType.MIR -> "MIR"
        CardType.UNKNOWN -> "UNKNOWN"
    }
}

private fun cardBackgroundBrush(cardType: CardType): Brush {
    return when (cardType) {
        CardType.VISA -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF0B2542),
                Color(0xFF194C7F),
                Color(0xFF4278A8),
            ),
        )
        CardType.MASTERCARD -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF291A15),
                Color(0xFF75411F),
                Color(0xFFB6652C),
            ),
        )
        CardType.MIR -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF10372A),
                Color(0xFF1D6A4C),
                Color(0xFF2FA26F),
            ),
        )
        CardType.UNKNOWN -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF1D2430),
                Color(0xFF435166),
                Color(0xFF6B798B),
            ),
        )
    }
}
