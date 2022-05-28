package dev.seriy.blogv2.routes

import dev.seriy.blogv2.models.BlogPost
import dev.seriy.blogv2.models.BlogPostList
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.nio.ByteBuffer

fun Route.blogRouting() {
    route("/blogs") {
        get {
            if (BlogPostList.isNotEmpty()) {
                call.respond(BlogPostList)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                BlogPostList.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
        post {
            try {
                val blogmodel = call.receive<BlogPost>()
                BlogPostList.add(blogmodel)
                call.respondText("Blog was successfully added", status = HttpStatusCode.Created)
            } catch (p: ContentTransformationException) {
                call.respondText("Check JSON e.g.", status = HttpStatusCode.BadRequest)
            }
        }
    }
    var fileDescription = ""
    var fileName = ""
    post("/upload") {
        val multipartData = call.receiveMultipart()

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }
                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    var fileBytes = part.streamProvider().readBytes()
                    File(
                        if (System.getProperty("os.name")
                                .startsWith("Windows")
                        ) "G:\\Server blogv2\\$fileName" else "/uploads/$fileName"
                    ).writeBytes(fileBytes)
                }
            }
        }

        call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
    }
}