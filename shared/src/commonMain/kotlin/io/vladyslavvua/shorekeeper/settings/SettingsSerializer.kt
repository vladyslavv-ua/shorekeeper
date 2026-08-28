package io.vladyslavvua.shorekeeper.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object SettingsSerializer : Serializer<ShorekeeperSettings> {

    override val defaultValue: ShorekeeperSettings = ShorekeeperSettings()

    private val json = Json {
        prettyPrint = true
    }

    override suspend fun readFrom(input: InputStream): ShorekeeperSettings =
        try {
            json.decodeFromString<ShorekeeperSettings>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: ShorekeeperSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}
