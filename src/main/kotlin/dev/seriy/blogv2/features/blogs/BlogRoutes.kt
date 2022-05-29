package dev.seriy.blogv2.features.blogs

import dev.seriy.blogv2.database.blogs.Blogs
import dev.seriy.blogv2.database.blogs.Blogs.deleteDirectory
import dev.seriy.blogv2.features.blogs.models.BlogModel
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.exists
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

fun Application.configureBlogRouting() {
    routing {
        get("/blogs/{uuid}") {
            val id = call.parameters["uuid"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            if (UUID.fromString(id) is UUID) {
                call.respond(Blogs.getUser(UUID.fromString(id)) ?: return@get call.respond(HttpStatusCode.NotFound))
            } else return@get call.respond(HttpStatusCode.BadRequest)
        }
        post("/blogs/{uuid}") {
            val uuid = call.parameters["uuid"] ?: return@post call.respondText(
                "Missing uuid",
                status = HttpStatusCode.BadRequest
            )
            if (Blogs.exists(UUID.fromString(uuid))) {
                val multipartData = call.receiveMultipart()
                var pos = 0
                val dir = (if (System.getProperty("os.name")
                        .startsWith("Windows")
                ) "G:\\uploads\\$uuid" else "/uploads/$uuid")
                if (File(dir).exists())
                    deleteDirectory(Path(dir))
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val fileBytes = part.streamProvider().readBytes()
                            val dir = File(
                                if (System.getProperty("os.name")
                                        .startsWith("Windows")
                                ) "G:\\uploads\\$uuid" else "/uploads/$uuid"
                            )
                            dir.mkdirs()
                            println("------------------------>"+File("/").list().joinToString(", "))
                            val type = part.originalFileName.toString().substringAfter('.')
                            File(
                                if (System.getProperty("os.name")
                                        .startsWith("Windows")
                                ) "G:\\uploads\\$uuid\\$pos.$type" else "/uploads/$uuid/$pos.$type"
                            ).writeBytes(fileBytes)
                            pos++
                        }
                    }
                    call.respondText("Image was created", status = HttpStatusCode.Created)
                }
            } else {
                call.respondText("Blog is not created", status = HttpStatusCode.NotFound)
            }
        }
        post("/blogs") {
            try {
                val model = call.receive(BlogModel::class)
                Blogs.insert(model)
                call.respond(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
