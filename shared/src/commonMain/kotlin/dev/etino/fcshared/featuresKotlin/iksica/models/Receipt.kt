package dev.etino.fcshared.featuresKotlin.iksica.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Receipt(
    val restaurant: String,
    val date: LocalDate,
    val dateString: String,
    val time: String,
    val receiptAmountInCents: Int,
    val subsidizedAmountInCents: Int,
    val paidAmountInCents: Int,
    val authorised: String,
    val url: String,
    var receiptDetails: List<ReceiptItem>? = null
) {
    constructor(receiptRoom: ReceiptRoom) : this(
        restaurant = receiptRoom.restaurant ?: "",
        date = LocalDate.parse(receiptRoom.date.toString()),
        dateString = receiptRoom.dateString ?: "",
        time = receiptRoom.time ?: "",
        receiptAmountInCents = receiptRoom.receiptAmountInCents ?: 0,
        subsidizedAmountInCents = receiptRoom.subsidizedAmountInCents ?: 0,
        paidAmountInCents = receiptRoom.paidAmountInCents ?: 0,
        authorised = receiptRoom.authorised ?: "",
        url = receiptRoom.href ?: ""
    )
}

@Entity
data class ReceiptRoom @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey
    var id: String = Uuid.generateV4().toString(),
    var restaurant: String? = null,
    var date: String? = null,
    var dateString: String? = null,
    var time: String? = null,
    var receiptAmountInCents: Int? = null,
    var subsidizedAmountInCents: Int? = null,
    var paidAmountInCents: Int? = null,
    var authorised: String? = null,
    var href: String? = null
) {
    constructor(receipt: Receipt) : this() {
        restaurant = receipt.restaurant
        date = receipt.date.toString()
        dateString = receipt.dateString
        time = receipt.time
        receiptAmountInCents = receipt.receiptAmountInCents
        subsidizedAmountInCents = receipt.subsidizedAmountInCents
        paidAmountInCents = receipt.paidAmountInCents
        authorised = receipt.authorised
        href = receipt.url
    }
}