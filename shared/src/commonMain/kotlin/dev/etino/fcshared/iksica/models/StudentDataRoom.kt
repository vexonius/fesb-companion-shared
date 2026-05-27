package dev.etino.fcshared.iksica.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudentDataRoom(
    @PrimaryKey
    var id: String,
    var imageUrl: String?,
    var nameSurname: String,
    var rightsLevel: String,
    var dailySupportInCents: Int,
    var oib: String,
    var jmbag: String,
    var cardNumber: String,
    var rightsFrom: String,
    var rightsTo: String,
    var balanceInCents: Int,
    var spentTodayInCents: Int,
) {

    constructor(studentData: StudentData) : this(
        id = ID,
        imageUrl = studentData.imageUrl,
        nameSurname = studentData.nameSurname,
        rightsLevel = studentData.rightsLevel,
        dailySupportInCents = studentData.dailySupport,
        oib = studentData.oib,
        jmbag = studentData.jmbag,
        cardNumber = studentData.cardNumber,
        rightsFrom = studentData.rightsFrom,
        rightsTo = studentData.rightsTo,
        balanceInCents = studentData.balanceInCents,
        spentTodayInCents = studentData.spentTodayInCents
    )

    companion object {
        const val ID = "1"
    }
}

