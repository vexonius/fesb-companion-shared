package dev.etino.fcshared.features.iksica

import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.days_ago_multiple
import fesb_companion_shared.composeapp.generated.resources.days_ago_one
import fesb_companion_shared.composeapp.generated.resources.in_the_future
import fesb_companion_shared.composeapp.generated.resources.today
import fesb_companion_shared.composeapp.generated.resources.yesterday
import org.jetbrains.compose.resources.StringResource
import kotlin.math.round


fun formatBalance(balance: Int?): String? {
    if (balance == null) return null
    val euros = (balance / 100).toString()
    val cents = (balance % 100).toString().padStart(2, '0')
    return "$euros,$cents"
}

fun formatBalance(balance: Int): String {
    val euros = (balance / 100).toString()
    val cents = (balance % 100).toString().padStart(2, '0')
    return "$euros,$cents"
}

/**
 * Rounds the Double to two decimal places by using BigDecimal.
 */
fun Double.roundToTwo(): String {
    return (round(this * 100) / 100).toString()
}

fun Long.daysAgoText(): Pair<StringResource, Long?> {
    return when {
        this == 0L -> Pair(Res.string.today, null)
        this == 1L -> Pair(Res.string.yesterday, null)
        this > 1L && this % 10 == 1L && this % 100 != 11L ->
            Pair(Res.string.days_ago_one, this)

        this > 1L ->
            Pair(Res.string.days_ago_multiple, this)

        else -> Pair(Res.string.in_the_future, null)
    }
}

