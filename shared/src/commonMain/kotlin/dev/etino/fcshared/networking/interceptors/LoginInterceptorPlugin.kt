package dev.etino.fcshared.networking.interceptors

import io.ktor.client.HttpClientConfig

interface LoginInterceptorPlugin {

    @Throws(Exception::class)
    fun setup(config: HttpClientConfig<*>)

}