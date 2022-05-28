package dev.seriy.blogv2

import dev.seriy.blogv2.features.blogs.configureBlogRouting
import dev.seriy.blogv2.plugins.configureRouting
import dev.seriy.blogv2.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        "postgres://qxliufstebbvlw:fec68196fb1633b0a475b9f097e3cf19da8e9e2340f685c7e9ddcd13167d50ab@ec2-3-211-221-185.compute-1.amazonaws.com:5432/d8tvpluk9c1mu3", driver = "org.postgresql.Driver",
        password = "fec68196fb1633b0a475b9f097e3cf19da8e9e2340f685c7e9ddcd13167d50ab", user = "qxliufstebbvlw"
    )

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureBlogRouting()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}