package dev.seriy.blogv2

import dev.seriy.blogv2.features.blogs.configureBlogRouting
import dev.seriy.blogv2.features.users.configureUserRouting
import dev.seriy.blogv2.plugins.configureRouting
import dev.seriy.blogv2.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5434/blogv2", driver = "org.postgresql.Driver",
        password = "ghjuhfvvf", user = "postgres"
    )

    embeddedServer(Netty, 8080, host = "0.0.0.0") {
        configureBlogRouting()
        configureRouting()
        configureUserRouting()
        configureSerialization()
    }.start(wait = true)
}