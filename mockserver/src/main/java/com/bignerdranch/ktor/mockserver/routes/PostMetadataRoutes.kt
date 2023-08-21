package com.bignerdranch.ktor.mockserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.postMetadataRouting() {
    route("/post-metadata") {
        get {
            val posts = javaClass.getResource("/files/post-metadata/all.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
            call.respondText(posts.readText())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            val post = javaClass.getResource("/files/post-metadata/$id.json")
                ?: return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)
            call.respondText(post.readText())
        }
    }
}

fun Application.registerPostMetadataRoutes() {
    routing {
        postMetadataRouting()
    }
}