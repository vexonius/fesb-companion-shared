package dev.etino.fcshared.menza.repository

import dev.etino.fcshared.menza.MenzaResult
import dev.etino.fcshared.menza.models.MenzaLocationType
import dev.etino.fcshared.menza.parseMenza
import dev.etino.fcshared.menza.service.MenzaServiceInterface
import dev.etino.fcshared.networking.NetworkServiceResult

class MenzaRepository(
    private val menzaNetworkService: MenzaServiceInterface,
) : MenzaRepositoryInterface {
    override suspend fun fetchMenzaDetails(place: MenzaLocationType, insert: Boolean): MenzaResult {
        return when (val result = menzaNetworkService.fetchMenza(place.string)) {
            is NetworkServiceResult.MenzaResult.Success -> {
                val parsed = parseMenza(result.data)
                if (parsed != null) {
                    MenzaResult.Success(parsed)
                } else {
                    MenzaResult.Failure(Throwable("Menies parsing error"))
                }
            }

            is NetworkServiceResult.MenzaResult.Failure -> {
                MenzaResult.Failure(Throwable("Menies fetching error"))
            }
        }
    }
}