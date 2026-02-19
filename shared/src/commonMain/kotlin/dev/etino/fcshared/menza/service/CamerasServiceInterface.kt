package dev.etino.fcshared.menza.service

import dev.etino.fcshared.menza.CamerasResult

interface CamerasServiceInterface {

    suspend fun getCameraImageUrls(path: String): CamerasResult.GetCamerasResult

}