package dev.etino.fcshared.featuresCompose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.etino.fcshared.compose.AppTheme
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.about_app
import fesb_companion_shared.shared.generated.resources.arrow_back_24px
import fesb_companion_shared.shared.generated.resources.category_user
import fesb_companion_shared.shared.generated.resources.contribute
import fesb_companion_shared.shared.generated.resources.data_privacy
import fesb_companion_shared.shared.generated.resources.developer_names
import fesb_companion_shared.shared.generated.resources.developers
import fesb_companion_shared.shared.generated.resources.help_improve_app
import fesb_companion_shared.shared.generated.resources.help_stabilize_app
import fesb_companion_shared.shared.generated.resources.jsoup_desc
import fesb_companion_shared.shared.generated.resources.jsoup_title
import fesb_companion_shared.shared.generated.resources.library_licenses
import fesb_companion_shared.shared.generated.resources.logged_in_as
import fesb_companion_shared.shared.generated.resources.logout
import fesb_companion_shared.shared.generated.resources.ok_http_desc
import fesb_companion_shared.shared.generated.resources.ok_http_title
import fesb_companion_shared.shared.generated.resources.privacy_policy_desc
import fesb_companion_shared.shared.generated.resources.privacy_policy_title
import fesb_companion_shared.shared.generated.resources.report_bug
import fesb_companion_shared.shared.generated.resources.send_feedback
import fesb_companion_shared.shared.generated.resources.settings
import fesb_companion_shared.shared.generated.resources.version
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

val leftPadding = 10.dp
val listItemStartPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCompose(
    viewModel: SettingsViewModel = koinViewModel(),
    paddingValues: PaddingValues,
    goBack: ()->Unit,
) {
    Scaffold(
        Modifier.padding(paddingValues),
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 4.dp),
                title = { Text(stringResource(Res.string.settings), style = MaterialTheme.typography.displaySmall) },
                navigationIcon = {
                    Icon(
                        painterResource(Res.drawable.arrow_back_24px),
                        "Nazad",
                        modifier = Modifier.padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .clickable{
                            goBack()
                        }.padding(8.dp)
                    )
                },
                windowInsets = WindowInsets()
            )
        }
    ) { contentPadding ->
        BottomSheetScaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            sheetPeekHeight = 0.dp,
            sheetContent = {
                if (viewModel.displayLicences.collectAsState().value) {
                    ModalBottomSheet(onDismissRequest = { viewModel.hideLicensesDialog() }) {

                        LazyColumn {
                            item {
                                LicenceItem(
                                    title = stringResource(Res.string.ok_http_title),
                                    supportText = stringResource(Res.string.ok_http_desc)
                                )
                            }
                            item {
                                LicenceItem(
                                    title = stringResource(Res.string.jsoup_title),
                                    supportText = stringResource(Res.string.jsoup_desc)
                                )
                            }
                            item {
                                LicenceItem(
                                    title = stringResource(Res.string.privacy_policy_title),
                                    supportText = stringResource(Res.string.privacy_policy_desc)
                                )
                            }

                        }
                    }
                }
            })
        {
            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                CategoryTitle(title = stringResource(Res.string.category_user))
                SettingsItem(
                    title = stringResource(Res.string.logout),
                    supportText = stringResource(
                        Res.string.logged_in_as,
                        viewModel.username.collectAsState().value
                    ),
                    onClick = {
                        viewModel.logout()
                    }
                )
                CategoryTitle(title = stringResource(Res.string.contribute))
                SettingsItem(
                    title = stringResource(Res.string.send_feedback),
                    supportText = stringResource(Res.string.help_improve_app),
                    onClick = {
                        //router.sendEmail(viewModel.getSupportEmailModalModel())
                    }
                )
                SettingsItem(
                    title = stringResource(Res.string.report_bug),
                    supportText = stringResource(Res.string.help_stabilize_app),
                    onClick = {
                        //router.sendEmail(viewModel.getBugReportEmailModalModel())
                    }
                )
                CategoryTitle(title = stringResource(Res.string.about_app))
                SettingsItem(
                    title = stringResource(Res.string.version),
                    supportText = viewModel.version.collectAsState().value
                )
                SettingsItem(
                    title = stringResource(Res.string.developers),
                    supportText = stringResource(Res.string.developer_names)
                )
                SettingsItem(
                    title = stringResource(Res.string.data_privacy),
                    supportText = null,
                    onClick = {
                        // router.openCustomTab(SettingsViewModel.Companion.pivacyUrl)
                    }
                )
                SettingsItem(
                    title = stringResource(Res.string.library_licenses),
                    supportText = null,
                    onClick = {
                        viewModel.displayLicensesDialog()
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryTitle(title: String) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    Box(
        modifier = Modifier
            .padding(
                start = listItemStartPadding,
                end = listItemStartPadding,
                top = 20.dp,
                bottom = 0.dp
            )
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = leftPadding),
            color = MaterialTheme.colorScheme.secondaryContainer,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun SettingsItem(
    title: String,
    supportText: String?,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick() },
        headlineContent = {
            Text(
                text = title,
                modifier = Modifier.padding(start = leftPadding)
            )
        },
        supportingContent = {
            supportText?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(start = leftPadding)
                )
            }
        }
    )
}

@Composable
fun SettingsCheckbox(
    title: String,
    supportText: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                modifier = Modifier.padding(start = leftPadding)
            )
        },
        supportingContent = {
            supportText?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(start = leftPadding)
                )
            }
        },
        trailingContent = {
            val darkenBy = 0.6f
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary.darken(darkenBy),
                    checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer.darken(darkenBy),
                    checkedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    uncheckedBorderColor = MaterialTheme.colorScheme.secondaryContainer.darken(darkenBy),
                )
            )
        }
    )
}

fun Color.darken(darkenBy: Float = 0.3f): Color {
    return copy(
        red = red * darkenBy,
        green = green * darkenBy,
        blue = blue * darkenBy,
        alpha = alpha
    )
}

@Composable
fun LicenceItem(
    title: String,
    supportText: String?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        ListItem(headlineContent = { Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold) })
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp, 15.dp)
        ) { Text(text = supportText ?: "", modifier = Modifier.padding(start = leftPadding)) }
    }
    HorizontalDivider()
}

@Preview
@Composable
fun PreviewSettingsCompose() {
    AppTheme {
        Column {
            CategoryTitle(title = "KORISNIK")
            SettingsItem(
                title = "Odjava",
                supportText = "Prijavljeni ste kao Ime Prezime",
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingsToggleCompose() {
    val mutableStateOf = remember { mutableStateOf(false) }
    AppTheme {
        Column {
            CategoryTitle(title = "KORISNIK")
            SettingsCheckbox(
                title = "Odjava",
                supportText = "Prijavljeni ste kao Ime Prezime",
                onCheckedChange = { mutableStateOf.value = !mutableStateOf.value },
                checked = mutableStateOf.value
            )
        }
    }
}
