package com.bignerdranch.ktor.mockserver

import com.bignerdranch.ktor.mockserver.routes.registerAuthorImageRoutes
import com.bignerdranch.ktor.mockserver.routes.registerPostMetadataRoutes
import com.bignerdranch.ktor.mockserver.routes.registerPostRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = 8106
    ) {
        install(ContentNegotiation) {
            json()
        }

        registerPostMetadataRoutes()
        registerPostRoutes()
        registerAuthorImageRoutes()
    }.start(wait = true)
}