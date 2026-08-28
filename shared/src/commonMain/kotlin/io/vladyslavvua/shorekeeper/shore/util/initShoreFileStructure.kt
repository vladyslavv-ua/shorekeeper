package io.vladyslavvua.shorekeeper.shore.util

import io.vladyslavvua.shorekeeper.shore.entity.Shore
import kotlinx.serialization.json.Json
import java.io.File


fun initShoreFileStructure(shore: Shore, path: String): Boolean {
    val file = File(path)
    if (!file.exists()) {
        file.mkdirs()
    }
    val shoreFolders = listOf("migrations", "queries", "views")
    shoreFolders.forEach {
        File(path, it).mkdirs()
    }
    val json = Json { prettyPrint = true }
    val shoreJson = json.encodeToString(shore)
    File(path, "shore.json").writeText(shoreJson)
    return true
}