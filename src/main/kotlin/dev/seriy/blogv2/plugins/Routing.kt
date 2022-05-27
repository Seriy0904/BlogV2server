package dev.seriy.blogv2.plugins

import dev.seriy.blogv2.routes.blogRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        blogRouting()
        get("/") {
            call.respond("Hello")
        }
    }
}
