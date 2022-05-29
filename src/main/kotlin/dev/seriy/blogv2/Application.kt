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
    val props = Properties()
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
    props.setProperty("dataSource.user", "qxliufstebbvlw")
    props.setProperty("dataSource.password", "fec68196fb1633b0a475b9f097e3cf19da8e9e2340f685c7e9ddcd13167d50ab")
    props.setProperty("dataSource.databaseName", "d8tvpluk9c1mu3")
    props["dataSource.logWriter"] = PrintWriter(System.out)
    val config = HikariConfig(props)
    val ds = HikariDataSource(config)
    Database.connect(ds)

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureBlogRouting()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}