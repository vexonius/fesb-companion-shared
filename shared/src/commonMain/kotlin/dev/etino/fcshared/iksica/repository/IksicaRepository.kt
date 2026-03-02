package dev.etino.fcshared.iksica.repository

import dev.etino.fcshared.iksica.dao.IksicaDao
import dev.etino.fcshared.iksica.models.IksicaData
import dev.etino.fcshared.iksica.models.IksicaResult
import dev.etino.fcshared.iksica.models.Receipt
import dev.etino.fcshared.iksica.models.ReceiptRoom
import dev.etino.fcshared.iksica.models.StudentData
import dev.etino.fcshared.iksica.models.StudentDataRoom
import dev.etino.fcshared.iksica.parseDetaljeRacuna
import dev.etino.fcshared.iksica.parseRacuni
import dev.etino.fcshared.iksica.parseStudentInfo
import dev.etino.fcshared.iksica.services.IksicaServiceInterface
import dev.etino.fcshared.networking.NetworkServiceResult

class IksicaRepository(
    private val iksicaService: IksicaServiceInterface,
    private val iksicaDao: IksicaDao,
) : IksicaRepositoryInterface {

    override suspend fun getCardDataAndReceipts(): IksicaResult.CardAndReceiptsResult {
        val studentInfo = getStudentInfo()

        when (val receiptsResult = getReceipts(studentInfo.oib)) {
            is IksicaResult.ReceiptsResult.Success -> {
                val receipts = receiptsResult.data
                insert(receipts)
                insert(studentInfo)

                return IksicaResult.CardAndReceiptsResult.Success(IksicaData(studentInfo, receipts))
            }

            is IksicaResult.ReceiptsResult.Failure -> {
                return IksicaResult.CardAndReceiptsResult.Failure(receiptsResult.throwable)
            }
        }
    }

    override suspend fun getReceipt(url: String): IksicaResult.ReceiptResult {
        return when (val result = iksicaService.getReceipt(url)) {
            is NetworkServiceResult.IksicaResult.Success -> {
                IksicaResult.ReceiptResult.Success(parseDetaljeRacuna(result.data))
            }

            is NetworkServiceResult.IksicaResult.Failure -> {
                //Log.e(TAG, "Receipt fetching error")
                if (result.throwable.message?.contains("Not logged in", false) == true) {
                    return IksicaResult.ReceiptResult.Failure(Throwable("Not logged in"))
                }
                IksicaResult.ReceiptResult.Failure(Throwable("Receipt fetching error" + result.throwable.message))
            }
        }
    }

    override suspend fun insert(model: StudentData) {
        iksicaDao.insert(StudentDataRoom(model))
    }

    override suspend fun insert(model: List<Receipt>) {
        iksicaDao.deleteAll()
        iksicaDao.insert(model.map { ReceiptRoom(it) })
    }

    override suspend fun getCache(): IksicaData? {
        val model = iksicaDao.readData()
        if (model == null) return null

        val receipts = iksicaDao.readReceipts()?.map { Receipt(it) }

        return IksicaData(StudentData(model), receipts)
    }

    private suspend fun getStudentInfo(): StudentData {
        when (val result = iksicaService.getStudentInfo()) {
            is NetworkServiceResult.IksicaResult.Success -> {
                val data = parseStudentInfo(result.data)

                return data
            }

            is NetworkServiceResult.IksicaResult.Failure -> {
                //Log.e(TAG, result.throwable.message ?: "AspNetSessionSAML fetching error")
                throw Exception(result.throwable.message ?: "AspNetSessionSAML fetching error")
            }
        }
    }

    private suspend fun getReceipts(oib: String): IksicaResult.ReceiptsResult {
        when (val result = iksicaService.getReceipts(oib)) {
            is NetworkServiceResult.IksicaResult.Success -> {
                val receiptsList = parseRacuni(result.data)

                return IksicaResult.ReceiptsResult.Success(receiptsList)
            }

            is NetworkServiceResult.IksicaResult.Failure -> {
                //Log.e(TAG, "Receipts fetching error")
                if (result.throwable.message?.contains("nema računa u zadnjih 30 dana", false) == true) {
                    return IksicaResult.ReceiptsResult.Success(emptyList())
                }
                if (result.throwable.message?.contains("Not logged in", false) == true) {
                    return IksicaResult.ReceiptsResult.Failure(Throwable("Not logged in"))
                }

                return IksicaResult.ReceiptsResult.Failure(Throwable("Receipts fetching error: " + result.throwable.message))
            }
        }
    }

}
