package dev.etino.fcshared.networking

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol.Companion.HTTPS

object Endpoints {

    private const val baseUrl = "raspored.fesb.unist.hr"

    val attendanceUrl = URLBuilder(
        protocol = HTTPS,
        host = baseUrl
    )

    val tableOverviewUrl = URLBuilder(
        protocol = HTTPS,
        host = baseUrl,
        pathSegments = listOf(
            "part",
            "prisutnost",
            "opcenito",
            "tablica"
        )
    )

}