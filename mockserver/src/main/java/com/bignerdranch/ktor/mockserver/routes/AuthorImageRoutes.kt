package com.bignerdranch.ktor.mockserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.authorImageRouting() {
    route("/images/authors") {
        get("{author}") {
            val author = call.parameters["author"] ?: return@get call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            val image = javaClass.getResourceAsStream("/files/images/authors/$author")
                ?: return@get call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )

            call.respond(
                withContext(Dispatchers.IO) {
                    image.readBytes()
                }
            )
        }
    }
}

fun Application.registerAuthorImageRoutes() {
    routing {
        authorImageRouting()
    }
}