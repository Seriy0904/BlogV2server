package dev.seriy.blogv2

import dev.seriy.blogv2.plugins.configureRouting
import dev.seriy.blogv2.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import java.sql.DatabaseMetaData

fun main() {
    embeddedServer(Netty, port = 5432, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}