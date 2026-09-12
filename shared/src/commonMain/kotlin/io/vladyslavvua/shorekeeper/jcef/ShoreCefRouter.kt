package io.vladyslavvua.shorekeeper.jcef

import io.vladyslavvua.shorekeeper.jcef.dto.JsToKotlinRequest
import io.vladyslavvua.shorekeeper.jcef.dto.KotlinToJsResponse
import io.vladyslavvua.shorekeeper.jcef.usecase.QueryProcessor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefQueryCallback
import org.cef.handler.CefMessageRouterHandlerAdapter
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.PreparedStatement

class ShoreCefRouter(
    private val connection: Connection,
    private val queriesFolder: String
) : CefMessageRouterHandlerAdapter() {
    private val queryProcessor = QueryProcessor(queriesFolder, connection)

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
                executeQuery(request.params)
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
        return postExecute(hasResultSet, statement, output)

    }

    private fun executeQuery(params: Map<String, JsonElement>): KotlinToJsResponse {
        val queryName = params["query"] ?: return KotlinToJsResponse(false)
        val queryParams = params["params"]!!.jsonArray
        val output = params["output"]?.jsonPrimitive?.content ?: "JSON"
        val statement = queryProcessor.prepareQuery(queryName.jsonPrimitive.content, queryParams)
        val hasResultSet = statement.execute()



        return postExecute(hasResultSet, statement, output)

    }


    private fun postExecute(hasResultSet: Boolean, statement: PreparedStatement, output: String): KotlinToJsResponse {

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