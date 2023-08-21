package com.bignerdranch.ktor.mockserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.delay

fun Route.postRouting() {
    route("/post") {
        get {
            val posts = javaClass.getResource("/files/post/all.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
            call.respondText(posts.readText())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            val post = javaClass.getResource("/files/post/$id.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)

            // this simulates a network latency for the candidate to address
            delay(if (id == "4") 4_000 else 250)

            call.respondText(post.readText())
        }
    }
}

fun Application.registerPostRoutes() {
    routing {
        postRouting()
    }
}