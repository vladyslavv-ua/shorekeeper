package io.vladyslavvua.shorekeeper.jcef.usecase

import io.vladyslavvua.shorekeeper.shore.entity.ShoreQuery
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.ObjectInput
import java.sql.Connection
import java.sql.PreparedStatement
import kotlin.collections.forEach

class QueryProcessor(private val queryPath: String, private val connection: Connection) {
    private val queries = initQueries(queryPath)

    private fun initQueries(queryPath: String): List<ShoreQuery> {
        val folder = File(queryPath)
        val queries = folder.listFiles()?.map {
            Json.decodeFromString<ShoreQuery>(it.readText())
        } ?: emptyList()
        return queries


    }

    fun prepareQuery(query: String, parameters: JsonArray): PreparedStatement {
        val query = queries.firstOrNull { it.name == query }
        if (query == null) {
            throw Exception("Query not found")
        }
        val preparedStatement = connection.prepareStatement(query.query)

        parameters.forEach { parameter ->
            val key = parameter.jsonObject["name"]?.jsonPrimitive?.content
            val value = parameter.jsonObject["value"]!!

            val keyInfo = query.parameters.firstOrNull { it.name == key }
            if (keyInfo == null) {
                throw Exception("Parameter not found")
            }
            val keyIndex = query.parameters.indexOf(keyInfo) + 1


            when (keyInfo.type) {
                ShoreQuery.ShoreQuery_Parameter.ShoreQuery_Parameter_Type.STRING -> {
                    preparedStatement.setString(keyIndex, value.jsonPrimitive.content)
                }

                ShoreQuery.ShoreQuery_Parameter.ShoreQuery_Parameter_Type.INT -> {
                    preparedStatement.setInt(keyIndex, value.jsonPrimitive.int)
                }

                ShoreQuery.ShoreQuery_Parameter.ShoreQuery_Parameter_Type.FLOAT -> {
                    preparedStatement.setFloat(keyIndex, value.jsonPrimitive.float)
                }

                ShoreQuery.ShoreQuery_Parameter.ShoreQuery_Parameter_Type.BOOLEAN -> {
                    preparedStatement.setBoolean(
                        keyIndex,
                        value.jsonPrimitive.boolean
                    )
                }

                else -> Unit
            }


        }

        return preparedStatement
    }


}