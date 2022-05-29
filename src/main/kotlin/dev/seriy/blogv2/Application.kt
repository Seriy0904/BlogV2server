package dev.seriy.blogv2

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.seriy.blogv2.features.blogs.configureBlogRouting
import dev.seriy.blogv2.plugins.configureRouting
import dev.seriy.blogv2.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import java.io.PrintWriter
import java.util.*


fun main() {
    val config = HikariConfig("hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureBlogRouting()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}