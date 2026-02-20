package dev.etino.fcshared.screens.login.models

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.autofill.ContentType
import kotlinx.coroutines.flow.MutableStateFlow

data class TextFieldModel(
    val text: MutableStateFlow<String>,
    val label: String,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(),
    val keyboardActions: KeyboardActions = KeyboardActions(),
    val textHidden: MutableStateFlow<Boolean>? = null,
    val trailingIcon: @Composable() (() -> Unit)? = null,
    val contentType: ContentType
)