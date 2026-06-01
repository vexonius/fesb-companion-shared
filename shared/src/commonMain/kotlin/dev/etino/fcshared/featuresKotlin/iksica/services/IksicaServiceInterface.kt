package dev.etino.fcshared.featuresKotlin.iksica.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface IksicaServiceInterface {

    suspend fun getStudentInfo(): NetworkServiceResult.IksicaResult

    suspend fun getReceipts(oib: String): NetworkServiceResult.IksicaResult

    suspend fun getReceipt(url: String): NetworkServiceResult.IksicaResult
}
