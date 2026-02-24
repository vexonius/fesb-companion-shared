package dev.etino.fcshared.iksica.services

import dev.etino.fcshared.networking.NetworkServiceResult

interface IksicaServiceInterface {

    suspend fun getStudentInfo(): NetworkServiceResult.IksicaResult

    suspend fun getReceipts(oib: String): NetworkServiceResult.IksicaResult

    suspend fun getReceipt(url: String): NetworkServiceResult.IksicaResult
}
