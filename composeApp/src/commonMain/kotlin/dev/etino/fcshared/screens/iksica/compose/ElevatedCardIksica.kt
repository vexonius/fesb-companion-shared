package dev.etino.fcshared.screens.iksica.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.compose.glowingColor
import dev.etino.fcshared.compose.gradientColors
import dev.etino.fcshared.iksica.models.StudentData
import dev.etino.fcshared.screens.iksica.formatBalance
import fesb_companion_shared.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ElevatedCardIksica(
    name: String = "Ime Prezime",
    iksicaNumber: String = "0000000000000000000",
    balance: Int = 0,
    onClick: () -> Unit = {}
) {
    val cornersRadius = 30.dp
    val glowingRadius = 100.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 30.dp, 24.dp, 50.dp)
            .aspectRatio(1.586f)
            .shadow(
                elevation = 50.dp,
                spotColor = glowingColor,
                ambientColor = glowingColor
            )
            .clip(shape = RoundedCornerShape(cornersRadius))
            .angledGradientBackground(colors = gradientColors, degrees = 32f)
    ) {
        Column(
            Modifier.clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .padding(25.dp)
                    .weight(0.7f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    val formattedNumber = buildList {
                        add(iksicaNumber.take(6))
                        add(iksicaNumber.drop(6).take(2))
                        add(iksicaNumber.drop(8).take(10))
                        add(iksicaNumber.takeLast(1))
                    }.joinToString(" ")
                    Column {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formattedNumber,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(
                            Res.string.iksica_balance,
                            formatBalance(balance)
                        ),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }
        }
    }
}


@Composable
fun CardIksicaPopupContent(studentInfo: StudentData) {
    AppTheme {
        Column(
            Modifier
                .padding(15.dp)
                .background(MaterialTheme.colorScheme.background)
                .width(300.dp)
        ) {
            CardIksicaPopupRow(leftText = stringResource(Res.string.name_label), rightText = studentInfo.nameSurname)
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.rights_level_label),
                rightText = studentInfo.rightsLevel
            )
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.daily_support_label), rightText = stringResource(
                    Res.string.iksica_balance, formatBalance(studentInfo.dailySupport)
                )
            )
            CardIksicaPopupRow(leftText = stringResource(Res.string.oib_label), rightText = studentInfo.oib)
            CardIksicaPopupRow(leftText = stringResource(Res.string.jmbag_label), rightText = studentInfo.jmbag)
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.card_number_label),
                rightText = studentInfo.cardNumber
            )
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.rights_from_label),
                rightText = studentInfo.rightsFrom
            )
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.right_until_label),
                rightText = studentInfo.rightsTo
            )
            CardIksicaPopupRow(
                leftText = stringResource(Res.string.card_balance_label), rightText = stringResource(
                    Res.string.iksica_balance, formatBalance(studentInfo.balanceInCents)
                ), divider = false
            )
        }
    }
}

@Composable
fun CardIksicaPopupRow(
    leftText: String, rightText: String, divider: Boolean = true
) {
    Row(
        Modifier
            .padding(20.dp, 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = leftText, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
        Text(text = rightText, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
    }
    if (divider) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}