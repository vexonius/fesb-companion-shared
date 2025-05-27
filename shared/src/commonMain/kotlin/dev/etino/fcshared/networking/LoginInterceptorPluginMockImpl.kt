package dev.etino.fcshared.networking

import dev.etino.fcshared.networking.LoginInterceptorPluginImpl.Companion.pluginName
import dev.etino.fcshared.user.UserService
import dev.etino.fcshared.user.UserServiceImpl
import fesb_companion_shared.shared.Secrets
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class LoginInterceptorPluginMockImpl(
    private val userService: UserService = UserServiceImpl(),
): LoginInterceptorPlugin {

    @Throws(Exception::class)
    override fun setup(config: HttpClientConfig<*>) {
        config.install(createClientPlugin(pluginName) {
            onRequest { request, content ->
                userService.login(Secrets.TEST_USERNAME, Secrets.TEST_PASSWORD)
            }
        })
    }

}