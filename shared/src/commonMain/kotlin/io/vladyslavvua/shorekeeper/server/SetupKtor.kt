package io.vladyslavvua.shorekeeper.server

import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File


class SetupKtor {

    fun setup(staticDir: String): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
        val port = 3566

        val server = embeddedServer(
            factory = Netty,
            port = port,
            module = { rootModule(staticDir) }
        )
//            .start(wait = false) // не блокує потік — сервер працює "як підпроцес"

        return server
    }



}

fun Application.rootModule(staticDir: String) {
    configureRouting(staticDir)

}

fun Application.configureRouting(staticDir: String) {

    println("staticDir: $staticDir")

    routing {
        staticFiles("/", File("$staticDir/views"))
    }
}