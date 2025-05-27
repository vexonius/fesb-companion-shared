package dev.etino.fcshared.networking

import io.ktor.client.HttpClientConfig

interface LoginInterceptorPlugin {

    @Throws(Exception::class)
    fun setup(config: HttpClientConfig<*>)

}