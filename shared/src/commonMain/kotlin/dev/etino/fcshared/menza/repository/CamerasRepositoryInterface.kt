package dev.etino.fcshared.menza.repository

import io.ktor.http.Url

interface CamerasRepositoryInterface {

    suspend fun getImages(path: String): Url?

}