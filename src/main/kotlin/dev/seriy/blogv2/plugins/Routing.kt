package dev.seriy.blogv2.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respond("WDWD")
        }
        get("/arr") {
            call.respond("AAAAAAAAAAAAAAAAAAA")
        }
    }
}
