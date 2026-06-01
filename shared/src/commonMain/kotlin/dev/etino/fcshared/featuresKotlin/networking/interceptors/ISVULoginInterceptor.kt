package dev.etino.fcshared.featuresKotlin.networking.interceptors

import dev.etino.fcshared.featuresKotlin.login.dao.UserDao
import dev.etino.fcshared.featuresKotlin.login.user.models.User
import dev.etino.fcshared.featuresKotlin.studomat.services.StudomatLoginServiceInterface
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

class ISVULoginInterceptor(
    private val studomatLoginService: StudomatLoginServiceInterface,
    private val userDao: UserDao
) {

    private val loginMutex = Mutex()

    @Volatile
    private var ongoingRefresh: CompletableDeferred<Unit>? = null

    suspend fun refreshSession() {
        if (loginMutex.isLocked) {
            ongoingRefresh?.await()
            return
        }
        val refreshJob = CompletableDeferred<Unit>().also { ongoingRefresh = it }
        loginMutex.withLock {
            try {
                val user = User(userDao.getUser())
                with(studomatLoginService) {
                    getSamlRequest()
                    sendSamlResponseToAAIEDU()
                    getSamlResponse(user.email, user.password)
                    sendSAMLToDecrypt()
                    sendSAMLToISVU()
                }
                refreshJob.complete(Unit)
            } catch (e: Exception) {
                refreshJob.completeExceptionally(e)
                throw e
            } finally {
                ongoingRefresh = null
            }
        }
    }

}