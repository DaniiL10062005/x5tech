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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
                elevation = 24.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor = Color(0x33111A25),
                spotColor = Color(0x33111A25),
            )
            .clip(RoundedCornerShape(34.dp))
            .background(cardBackgroundBrush(cardType))
            .padding(24.dp),
    ) {
        CardDecorations()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(99.dp),
                        color = Color.White.copy(alpha = 0.14f),
                    ) {
                        Text(
                            text = bankName ?: stringResource(Res.string.card_visual_bank_name_fallback),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.88f),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }

                    Text(
                        text = stringResource(Res.string.card_visual_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CardBrandMark(cardType = cardType)
                    Surface(
                        shape = RoundedCornerShape(99.dp),
                        color = Color.Black.copy(alpha = 0.18f),
                    ) {
                        Text(
                            text = if (isMasked) {
                                stringResource(Res.string.card_visual_protected)
                            } else {
                                stringResource(Res.string.card_visual_live_preview)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = stringResource(Res.string.card_visual_number_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.72f),
                )
                AnimatedCardText(
                    text = displayValue(
                        value = cardNumber,
                        fallback = stringResource(Res.string.card_visual_number_fallback),
                    ),
                    style = MaterialTheme.typography.headlineMedium.copy(letterSpacing = 1.2.sp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Surface(
                shape = RoundedCornerShape(26.dp),
                color = Color.White.copy(alpha = 0.12f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    CardMetaBlock(
                        label = stringResource(Res.string.card_visual_holder_label),
                        value = displayValue(
                            value = holderName,
                            fallback = stringResource(Res.string.card_visual_holder_fallback),
                        ),
                        modifier = Modifier.weight(1.15f),
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
}

@Composable
private fun BoxScope.CardDecorations() {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(148.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.Transparent,
                    ),
                ),
            ),
    )
    Box(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .size(width = 180.dp, height = 96.dp)
            .clip(RoundedCornerShape(topEnd = 96.dp, bottomEnd = 96.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.14f),
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.82f)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(99.dp),
            color = brandTint(cardType).copy(alpha = 0.9f),
        ) {
            Text(
                text = cardTypeLabel(cardType),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0D1320),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
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
        CardType.VISA -> Color(0xFF8FD3FF)
        CardType.MASTERCARD -> Color(0xFFFFB66B)
        CardType.MIR -> Color(0xFF8CE4B0)
        CardType.UNKNOWN -> Color(0xFFD8DEE7)
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
