package dev.etino.fcshared.featuresCompose.login.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.featuresCompose.login.models.TextFieldModel
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.login_action_submit
import fesb_companion_shared.shared.generated.resources.login_email_or_username
import fesb_companion_shared.shared.generated.resources.login_login_title
import fesb_companion_shared.shared.generated.resources.login_password
import fesb_companion_shared.shared.generated.resources.login_safe_data
import fesb_companion_shared.shared.generated.resources.visibility_hide
import fesb_companion_shared.shared.generated.resources.visibility_show
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginCompose(
    showLoading: MutableStateFlow<Boolean>,
    username: MutableStateFlow<String>,
    password: MutableStateFlow<String>,
    passwordHidden: MutableStateFlow<Boolean>,
    tryUserLogin: () -> Unit,
    showSnackbar: SharedFlow<StringResource>,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showLoadingObserved = showLoading.collectAsState().value

    fun onDone() {
        keyboardController?.hide()
        tryUserLogin()
    }

    LaunchedEffect(Unit) {
        showSnackbar.collect { resId ->
            snackbarHostState.showSnackbar(
                message = getString(resId)
            )
        }
    }

    val usernameModel = TextFieldModel(
        text = username,
        label = stringResource(Res.string.login_email_or_username),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        contentType = ContentType.Username + ContentType.EmailAddress
    )
    val passwordModel = TextFieldModel(
        text = password,
        label = stringResource(Res.string.login_password),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        textHidden = passwordHidden,
        trailingIcon = {
            IconButton(onClick = { passwordHidden.value = passwordHidden.value?.not() == true }) {
                val iconId =
                    if (passwordHidden.collectAsState().value) Res.drawable.visibility_show else Res.drawable.visibility_hide
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = "Visibility Icon",
                    modifier = Modifier.padding(7.dp)
                )
            }
        },
        contentType = ContentType.Password
    )

    Scaffold(
        Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    it,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(40.dp, 16.dp, 40.dp, 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(128.dp))
            Text(
                text = stringResource(Res.string.login_login_title),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.login_safe_data),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomTextField(usernameModel)
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(passwordModel)
            Spacer(modifier = Modifier.height(24.dp))
            ButtonCircularLoading(showLoadingObserved, ::onDone)
            Spacer(modifier = Modifier.height(64.dp))

        }
    }
}

@Composable
fun CustomTextField(textFieldModel: TextFieldModel) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
        focusedLabelColor = MaterialTheme.colorScheme.secondaryContainer,
        cursorColor = MaterialTheme.colorScheme.secondaryContainer,
    )
    val textFieldShape = RoundedCornerShape(10.dp)

    val customHighlightColor =
        Color.Transparent // removed color for autofill highlight because of a bad clip, can't clip properly because of the top letters
    CompositionLocalProvider(LocalAutofillHighlightColor provides customHighlightColor) {
        OutlinedTextField(
            value = textFieldModel.text.collectAsState().value,
            onValueChange = { textFieldModel.text.value = it },
            colors = textFieldColors,
            shape = textFieldShape,
            label = { Text(text = textFieldModel.label) },
            keyboardOptions = textFieldModel.keyboardOptions,
            keyboardActions = textFieldModel.keyboardActions,
            visualTransformation = if (textFieldModel.textHidden?.collectAsState()?.value == true) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = textFieldModel.trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = textFieldModel.contentType
                }
        )
    }
}


@Composable
fun ButtonCircularLoading(
    showLoadingObserved: Boolean?, onDone: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End
    ) {
        Box(
            Modifier
                .height(40.dp)
                .width(120.dp)
                .align(Alignment.CenterVertically)
        ) {
            if (showLoadingObserved == true) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(35.dp)
                        .padding(5.dp)
                        .align(Alignment.Center),
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round
                )
            } else {
                Button(
                    onClick = onDone,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.login_action_submit),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginComposePreview() {
    AppTheme{
        LoginCompose(
            MutableStateFlow(false),
            MutableStateFlow(""),
            MutableStateFlow(""),
            MutableStateFlow(true),
            {},
            MutableSharedFlow()
        )
    }
}