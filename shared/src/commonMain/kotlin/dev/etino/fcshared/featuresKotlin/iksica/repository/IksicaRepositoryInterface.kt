package dev.etino.fcshared.featuresKotlin.iksica.repository

import dev.etino.fcshared.featuresKotlin.iksica.models.IksicaData
import dev.etino.fcshared.featuresKotlin.iksica.models.IksicaResult
import dev.etino.fcshared.featuresKotlin.iksica.models.Receipt
import dev.etino.fcshared.featuresKotlin.iksica.models.StudentData

interface IksicaRepositoryInterface {

    suspend fun getCardDataAndReceipts(): IksicaResult.CardAndReceiptsResult

    suspend fun getReceipt(url: String): IksicaResult.ReceiptResult

    suspend fun insert(model: StudentData)

    suspend fun insert(model: List<Receipt>)

    suspend fun getCache(): IksicaData?

}
