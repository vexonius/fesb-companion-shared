package dev.etino.fcshared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
