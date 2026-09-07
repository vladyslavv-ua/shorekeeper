package io.vladyslavvua.shorekeeper.jcef

import io.ktor.util.StringValuesBuilder
import io.vladyslavvua.shorekeeper.jcef.dto.JsToKotlinRequest
import io.vladyslavvua.shorekeeper.jcef.dto.KotlinToJsResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefQueryCallback
import org.cef.handler.CefMessageRouterHandlerAdapter
import org.jooq.impl.DSL
import java.sql.Connection

class ShoreCefRouter(
    private val connection: Connection,
    private val queriesFolder: String
) : CefMessageRouterHandlerAdapter() {

    override fun onQuery(
        browser: CefBrowser,
        frame: CefFrame,
        queryId: Long,
        request: String,
        persistent: Boolean,
        callback: CefQueryCallback
    ): Boolean {
        val mappedRequest: JsToKotlinRequest

        try {
            mappedRequest = Json.decodeFromString<JsToKotlinRequest>(request)
        } catch (e: Exception) {
            callback.failure(1, "Invalid request")
            return false
        }

        val response = handleIntent(mappedRequest)
        if (response.success) {

            callback.success(Json.encodeToString(response))
        } else {
            callback.failure(0, "Invalid request")
        }

        return true
    }

    override fun onQueryCanceled(browser: CefBrowser, frame: CefFrame, queryId: Long) {
    }


    private fun handleIntent(request: JsToKotlinRequest): KotlinToJsResponse =
        when (request.method) {
            "executeQuery" -> {
                KotlinToJsResponse(true)
            }

            "executeRawQuery" -> executeRawQuery(request.params)

            else -> KotlinToJsResponse(false)


        }

    private fun executeRawQuery(params: Map<String, JsonElement>): KotlinToJsResponse {
        val query = params["query"] ?: return KotlinToJsResponse(false)
        val output = params["output"]?.jsonPrimitive?.content ?: "JSON"
        println("executeRawQuery: $query")
        val statement = connection.prepareStatement(query.jsonPrimitive.content)
        val hasResultSet = statement.execute()
        if (hasResultSet) {
            val resultSet = statement.resultSet

            if (output == "JSON") {
                val jsonObject = DSL.using(connection).fetch(resultSet).formatJSON()
                return KotlinToJsResponse(true, jsonObject)
            }

            val csv = DSL.using(connection).fetch(resultSet).formatCSV()
            return KotlinToJsResponse(true, csv)
        } else {
            val updateCount = statement.updateCount
            return KotlinToJsResponse(true, "OK, affected rows: $updateCount")
        }

    }
}