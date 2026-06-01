package dev.etino.fcshared.featuresKotlin.menza

import dev.etino.fcshared.featuresKotlin.menza.models.Menza

sealed class MenzaResult {
    data class Success(val data: Menza) : MenzaResult()
    class Failure(exception: Throwable) : MenzaResult()
}

sealed class CamerasResult {

    sealed class GetCamerasResult : CamerasResult() {
        data class Success(val data: String) : GetCamerasResult()
        data object Failure : GetCamerasResult()
    }

}