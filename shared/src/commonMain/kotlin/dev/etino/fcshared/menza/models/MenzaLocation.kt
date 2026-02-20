package dev.etino.fcshared.menza.models


data class MenzaLocation (
    val name: String,
    val address: String,
    val meniName: MenzaLocationType,
    val cameraName: String,
)