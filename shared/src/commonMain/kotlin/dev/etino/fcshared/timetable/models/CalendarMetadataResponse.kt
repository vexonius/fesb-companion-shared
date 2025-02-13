package dev.etino.fcshared.timetable.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarMetadataResponse(
    @SerialName("Id")
    val id: String,
    @SerialName("Name")
    val name: String,
    @SerialName("StartDate")
    val startDate: String,
    @SerialName("EndDate")
    val endDate: String,
    @SerialName("StartDateText")
    val startDateText: String,
    @SerialName("EndDateText")
    val endDateText: String,
    @SerialName("Category")
    val category: String,
    @SerialName("ColorCode")
    val colorCode: String,
    @SerialName("IsWorking")
    val isWorking: Boolean
)
