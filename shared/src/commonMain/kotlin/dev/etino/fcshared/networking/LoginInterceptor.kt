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

    @Throws(Exception::class)
    fun setup(config: HttpClientConfig<*>) {
        config.install(createClientPlugin(pluginName) {
            onRequest { request, content ->
                if (!cookieStorage.isFESBTokenValid()) {
                    runBlocking {
                        val username = secureStorage.string(SecureField.USERNAME.value)
                        val password = secureStorage.string(SecureField.PASSWORD.value)

                        if (username == null || password == null) {
                            throw  Exception("LoginInterceptorPlugin: Username or password is null")
                        }

                        userService.login(username, password)
                    }
                }
            }
        })
    }

    companion object {
        const val pluginName = "LoginInterceptorPlugin"
    }

}