package dev.etino.fcshared.featuresKotlin.iksica.models


sealed class IksicaResult {

    sealed class ReceiptResult : IksicaResult() {
        data class Success(val data: MutableList<ReceiptItem>) : ReceiptResult()
        class Failure(val throwable: Throwable) : ReceiptResult()
    }

    sealed class ReceiptsResult : IksicaResult() {
        data class Success(val data: List<Receipt>) : ReceiptsResult()
        class Failure(val throwable: Throwable) : ReceiptsResult()
    }

    sealed class CardAndReceiptsResult : IksicaResult() {
        data class Success(val data: IksicaData) : CardAndReceiptsResult()
        class Failure(val throwable: Throwable) : CardAndReceiptsResult()
    }

}