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
import java.io.File
import java.util.*
import kotlin.io.path.Path

fun Application.configureBlogRouting() {
    routing {
        get("/blogs/{uuid}") {
            val id = (call.parameters["uuid"]) ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            try {
                call.respond(Blogs.getUser(UUID.fromString(id)) ?: return@get call.respond(HttpStatusCode.NotFound))
            } catch (e: java.lang.IllegalArgumentException) {
                return@get call.respondText(
                    "Invalid UUID string: $id",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        get("/blogs/all") {
            call.respond(Blogs.getAll())
        }
        post("/blogs") {
            try {
                val model = call.receive(BlogModel::class)
                if (!Blogs.exists(model.uuid)) {
                    Blogs.insert(model)
                    call.respond(HttpStatusCode.Created)
                } else {
                    call.respondText("This blog already created", status = HttpStatusCode.NotFound)
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/blogs/{uuid}") {
            val uuid = call.parameters["uuid"] ?: return@post call.respondText(
                "Missing uuid",
                status = HttpStatusCode.BadRequest
            )
            try {
                if (Blogs.exists(UUID.fromString(uuid))) {
                    val multipartData = call.receiveMultipart()
                    var pos = 0
                    val dir = (if (System.getProperty("os.name")
                            .startsWith("Windows")
                    ) "G:\\uploads\\$uuid\\" else "/uploads/$uuid/")
                    if (File(dir).exists()) {
                        deleteDirectory(Path(dir))
                        File(dir).mkdirs()
                    } else
                        File(dir).mkdirs()
                    val arr = arrayListOf<String>()
                    multipartData.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                val fileBytes = part.streamProvider().readBytes()
                                val type = part.originalFileName.toString().substringAfter('.')
                                File(
                                    if (System.getProperty("os.name")
                                            .startsWith("Windows")
                                    ) "G:\\uploads\\$uuid\\$pos.$type" else "/uploads/$uuid/$pos.$type"
                                ).writeBytes(fileBytes)
                                arr.add("$dir$pos.$type")
                                pos++
                            }
                        }
                    }
                    Blogs.edit(UUID.fromString(uuid), photosurl = arr.joinToString(","))
                    call.respondText("Image was created", status = HttpStatusCode.Created)

                } else {
                    call.respondText("Blog is not created", status = HttpStatusCode.NotFound)
                }
            } catch (e: java.lang.IllegalArgumentException) {
                return@post call.respondText(
                    "Invalid UUID string: $uuid",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        delete("/blogs/{uuid}") {
            val uuid = call.parameters["uuid"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            try {
                Blogs.deleteBlog(UUID.fromString(uuid))
                call.respond(HttpStatusCode.OK)
            } catch (e: java.lang.IllegalArgumentException) {
                return@delete call.respondText(
                    "Invalid UUID string: $uuid",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
    }
}
