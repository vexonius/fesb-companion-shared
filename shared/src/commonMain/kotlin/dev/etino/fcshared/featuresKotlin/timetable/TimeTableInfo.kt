package dev.etino.fcshared.featuresKotlin.timetable


import dev.etino.fcshared.featuresKotlin.ColorSerializer
import dev.etino.fcshared.featuresKotlin.TimetableDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeTableInfo(
    @SerialName("Id")
    var id: Int = 0,
    @SerialName("Name")
    var name: String = "",
    @Serializable(with = TimetableDateSerializer::class)
    @SerialName("StartDate")
    var startDate: LocalDate,
    @Serializable(with = TimetableDateSerializer::class)
    @SerialName("EndDate")
    var endDate: LocalDate,
    @SerialName("StartDateText")
    var startDateText: String = "",
    @SerialName("EndDateText")
    var endDateText: String = "",
    @SerialName("Category")
    var category: String = "",
    @Serializable(with = ColorSerializer::class)
    @SerialName("ColorCode")
    var colorCode: Long = 0x00FFFFFF,
    @SerialName("IsWorking")
    var isWorking: Boolean = false,
)
