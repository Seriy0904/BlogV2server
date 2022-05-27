package dev.seriy.blogv2.routes

import dev.seriy.blogv2.models.BlogPost
import dev.seriy.blogv2.models.BlogPostList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.blogRouting(){
    route("/costumers"){
        get {
            if (BlogPostList.isNotEmpty()) {
                call.respond(BlogPostList)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}"){
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
            try{
                val blogmodel = call.receive<BlogPost>()
                BlogPostList.add(blogmodel)
                call.respondText("Blog was successfully added", status = HttpStatusCode.Created)
            }catch (p:Exception){
                call.respondText("Method Not Allowed", status = HttpStatusCode.MethodNotAllowed)
            }
        }
        delete("{id?}") {

        }
    }
}