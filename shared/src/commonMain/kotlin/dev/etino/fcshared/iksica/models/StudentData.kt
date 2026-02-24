package dev.etino.fcshared.iksica.models

data class StudentData(
    val imageUrl: String?,
    val nameSurname: String,
    val rightsLevel: String,
    val dailySupport: Int,
    val oib: String,
    val jmbag: String,
    val cardNumber: String,
    val rightsFrom: String,
    val rightsTo: String,
    val balanceInCents: Int,
    val spentTodayInCents: Int
) {

    constructor(studentDataRoom: StudentDataRoom) : this(
        imageUrl = studentDataRoom.imageUrl,
        rightsLevel = studentDataRoom.rightsLevel,
        dailySupport = studentDataRoom.dailySupportInCents,
        nameSurname = studentDataRoom.nameSurname,
        rightsTo = studentDataRoom.rightsTo,
        rightsFrom = studentDataRoom.rightsFrom,
        cardNumber = studentDataRoom.cardNumber,
        oib = studentDataRoom.oib,
        jmbag = studentDataRoom.jmbag,
        balanceInCents = studentDataRoom.balanceInCents,
        spentTodayInCents = studentDataRoom.spentTodayInCents
    )

    companion object {
        val empty = StudentData(
            imageUrl = null,
            nameSurname = "",
            rightsLevel = "",
            dailySupport = 0,
            oib = "",
            jmbag = "",
            cardNumber = "",
            rightsFrom = "",
            rightsTo = "",
            balanceInCents = 0,
            spentTodayInCents = 0
        )
    }
}
