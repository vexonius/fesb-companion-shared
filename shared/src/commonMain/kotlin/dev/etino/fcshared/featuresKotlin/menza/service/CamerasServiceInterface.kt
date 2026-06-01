package dev.etino.fcshared.featuresKotlin.menza.service

import dev.etino.fcshared.featuresKotlin.menza.CamerasResult

interface CamerasServiceInterface {

    suspend fun getCameraImageUrls(path: String): CamerasResult.GetCamerasResult

}