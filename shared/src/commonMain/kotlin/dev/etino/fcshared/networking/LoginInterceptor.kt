package dev.etino.fcshared.networking

import com.liftric.kvault.KVault
import dev.etino.fcshared.Utils.SecureField
import dev.etino.fcshared.user.UserService
import dev.etino.fcshared.user.UserServiceImpl
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.runBlocking

class LoginInterceptorPlugin(
    private val userService: UserService = UserServiceImpl(),
    private val cookieStorage: PortalCookieStorage = PortalCookieStorage,
    private val secureStorage: KVault
) {

    fun setup(config: HttpClientConfig<*>) {
        config.install(createClientPlugin(pluginName) {
            onRequest { request, content ->
                if (!cookieStorage.isFESBTokenValid()) {
                    runBlocking {
                        userService
                            .login(
                                secureStorage.string(SecureField.USERNAME.name) ?: "",
                                secureStorage.string(SecureField.PASSWORD.name) ?: ""
                            )
                    }
                }
            }
        })
    }

    companion object {
        const val pluginName = "LoginInterceptorPlugin"
    }

}