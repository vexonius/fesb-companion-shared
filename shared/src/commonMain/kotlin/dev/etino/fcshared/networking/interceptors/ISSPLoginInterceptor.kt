package dev.etino.fcshared.networking.interceptors

import dev.etino.fcshared.iksica.services.IksicaLoginServiceInterface
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.login.user.models.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

class ISSPLoginInterceptor(
    private val iksicaLoginService: IksicaLoginServiceInterface,
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
                with(iksicaLoginService) {
                    getAuthState()
                    login(user.email, user.password)
                    getAspNetSessionSAML()
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