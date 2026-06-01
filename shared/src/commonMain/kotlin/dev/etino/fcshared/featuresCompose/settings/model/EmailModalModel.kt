package dev.etino.fcshared.featuresCompose.settings.model

data class EmailModalModel(
    val recipient: String,
    val title: String,
    val subject: String,
    val body: String
)

