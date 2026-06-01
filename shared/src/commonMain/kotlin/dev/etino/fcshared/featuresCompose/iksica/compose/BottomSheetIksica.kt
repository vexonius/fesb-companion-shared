package dev.etino.fcshared.featuresCompose.iksica.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.featuresCompose.iksica.formatBalance
import dev.etino.fcshared.featuresKotlin.iksica.models.Receipt
import dev.etino.fcshared.featuresKotlin.iksica.models.ReceiptItem
import dev.etino.fcshared.featuresKotlin.now
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.amount_x
import fesb_companion_shared.shared.generated.resources.currency
import fesb_companion_shared.shared.generated.resources.price_of_item
import fesb_companion_shared.shared.generated.resources.subsidized_price_of_item
import fesb_companion_shared.shared.generated.resources.transaction_details
import fesb_companion_shared.shared.generated.resources.transaction_paid
import fesb_companion_shared.shared.generated.resources.transaction_subsidized
import fesb_companion_shared.shared.generated.resources.transaction_total
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetIksica(
    receipt: Receipt?,
    toggleShowItem: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { toggleShowItem() },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = { WindowInsets() },
        dragHandle = { },
    ) {
        IksicaReceiptDetailed(receipt)
    }
}

@Composable
fun IksicaReceiptDetailed(
    receipt: Receipt?
) {
    LazyColumn(
        Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 20.dp, 10.dp)
            ) {
                Text(
                    text = stringResource(Res.string.transaction_details),
                    color = MaterialTheme.contentColors.primary,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Text(
                    text = receipt?.restaurant ?: "",
                    color = MaterialTheme.contentColors.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = (receipt?.dateString ?: "") + ", " + (receipt?.time ?: ""),
                    color = MaterialTheme.contentColors.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        items(receipt?.receiptDetails ?: emptyList()) {
            IksicaItemDetailed(it)
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 10.dp)
                    .fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(Res.string.transaction_total),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.contentColors.primary
                        )
                        Text(
                            text = stringResource(Res.string.transaction_subsidized),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.contentColors.secondary
                        )
                        Text(
                            text = stringResource(Res.string.transaction_paid),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.contentColors.secondary,
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = formatBalance(receipt?.receiptAmountInCents) + stringResource(Res.string.currency),
                            color = MaterialTheme.contentColors.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatBalance(receipt?.subsidizedAmountInCents) + stringResource(Res.string.currency),
                            color = MaterialTheme.contentColors.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatBalance(receipt?.paidAmountInCents) + stringResource(Res.string.currency),
                            color = MaterialTheme.contentColors.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun IksicaItemDetailed(item: ReceiptItem) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, 5.dp, 15.dp, 5.dp)
    ) {

        Text(
            text = stringResource(Res.string.amount_x, item.amount.toString()),
            color = MaterialTheme.contentColors.primary,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = item.articleName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    text =
                        formatBalance((item.totalInCents - item.subsidizedAmountInCents) * item.amount) + stringResource(
                            Res.string.currency
                        ),
                    modifier = Modifier
                        .weight(0.20f)
                        .padding(start = 10.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.End
                )
            }
            Column {
                Text(
                    text = stringResource(
                        Res.string.price_of_item,
                        item.priceInCents.toString()
                    ) + stringResource(Res.string.currency),
                    color = MaterialTheme.contentColors.tertiary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(
                        Res.string.subsidized_price_of_item,
                        formatBalance(item.subsidizedAmountInCents)
                    ) + stringResource(Res.string.currency),
                    color = MaterialTheme.contentColors.tertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun IksicaItemPreview() {
    IksicaItem(
        receipt = Receipt(
            restaurant = "Restoran",
            dateString = "Datum",
            time = "Vrijeme",
            receiptDetails = listOf(
                ReceiptItem(
                    articleName = "Naziv",
                    amount = 1,
                    totalInCents = 55,
                    subsidizedAmountInCents = 27,
                    priceInCents = 58
                )
            ),
            receiptAmountInCents = 55,
            subsidizedAmountInCents = 27,
            paidAmountInCents = 55,
            authorised = "Autorizacija",
            url = "https://www.google.com",
            date = LocalDate.now()
        )
    ) {}
}


@Preview
@Composable
fun IksicaReceiptDetailedPreview() {
    Box(Modifier.background(MaterialTheme.colorScheme.background)) {
        IksicaReceiptDetailed(
            receipt = Receipt(
                restaurant = "Restoran",
                dateString = "Datum",
                time = "Vrijeme",
                receiptDetails = listOf(
                    ReceiptItem(
                        articleName = "Naziv",
                        amount = 1,
                        totalInCents = 55,
                        subsidizedAmountInCents = 27,
                        priceInCents = 58
                    ),
                    ReceiptItem(
                        articleName = "Naziv",
                        amount = 1,
                        totalInCents = 55,
                        subsidizedAmountInCents = 27,
                        priceInCents = 58
                    )
                ),
                receiptAmountInCents = 55,
                subsidizedAmountInCents = 27,
                paidAmountInCents = 55,
                authorised = "Autorizacija",
                url = "https://www.google.com",
                date = LocalDate.now()
            )
        )
    }
}