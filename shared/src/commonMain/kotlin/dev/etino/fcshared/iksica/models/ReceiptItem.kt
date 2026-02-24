package dev.etino.fcshared.iksica.models

data class ReceiptItem(
    val articleName: String,
    /**
     * Amount of the article.
     */
    var amount: Int,
    /**
     * Discounted price
     */
    val priceInCents: Int,
    /**
     * Full price
     */
    val totalInCents: Int,
    /**
     * Subsidized amount
     */
    val subsidizedAmountInCents: Int
)
