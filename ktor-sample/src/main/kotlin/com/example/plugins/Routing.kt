package com.example.plugins

import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val users = mutableListOf(
        User(1, "Misha", 22),
        User(2, "Alexandr", 25),
        User(3, "Rakhim", 22),
    )

    routing {
        get("/users") {
            call.respond(HttpStatusCode.OK, users)
        }

        post("/users") {
            val status = runCatching {
                val user = call.receive<User>()
                if (user.age >= 18) {
                    users.add(user.copy(id = users.last().id + 1L))
                    HttpStatusCode.Created
                } else HttpStatusCode.BadRequest
            }.getOrDefault(HttpStatusCode.BadRequest)
            call.respond(status)
        }
    }
}