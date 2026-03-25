package com.example.x5tech.feature.cardform

import androidx.compose.runtime.Composable
import com.example.x5tech.feature.cardform.resources.Res
import com.example.x5tech.feature.cardform.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

internal sealed interface UiText {
    data class Resource(
        val id: StringResource,
        val args: List<Any> = emptyList(),
    ) : UiText

    data class DynamicString(
        val value: String,
    ) : UiText
}

@Composable
internal fun UiText.asString(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.Resource -> stringResource(id, *args.toTypedArray())
    }
}

internal fun Throwable.toUiText(): UiText {
    return message
        ?.takeIf(String::isNotBlank)
        ?.let(UiText::DynamicString)
        ?: UiText.Resource(Res.string.card_form_save_error_default)
}
