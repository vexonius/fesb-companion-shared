package dev.etino.fcshared.screens.iksica.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.iksica.models.Receipt
import dev.etino.fcshared.now
import dev.etino.fcshared.screens.iksica.daysAgoText
import dev.etino.fcshared.screens.iksica.formatBalance
import dev.etino.fcshared.screens.iksica.roundToTwo
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.currency
import fesb_companion_shared.composeapp.generated.resources.minus_amount
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.until
import org.jetbrains.compose.resources.stringResource

@Composable
fun IksicaItem(receipt: Receipt, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp, 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                receipt.restaurant.trim(),
                color = MaterialTheme.contentColors.primary,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.80f)
            )
            Text(
                text = stringResource(
                    Res.string.minus_amount, formatBalance(receipt.subsidizedAmountInCents)
                ) + stringResource(Res.string.currency),
                color = MaterialTheme.contentColors.primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(0.20f),
                textAlign = TextAlign.End,
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row {
                val today = LocalDate.now()
                val daysAgo = receipt.date.until(today, DateTimeUnit.DAY).daysAgoText()
                //val daysAgo = ChronoUnit.DAYS.between(receipt.date, today).daysAgoText(LocalContext.current)
                Text(daysAgo.second?.let {
                    stringResource(daysAgo.first, it)
                } ?: stringResource(daysAgo.first),
                    color = MaterialTheme.contentColors.tertiary,
                    style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    receipt.time,
                    color = MaterialTheme.contentColors.tertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    HorizontalDivider(Modifier.padding(horizontal = 10.dp), color = MaterialTheme.colorScheme.outline)
}