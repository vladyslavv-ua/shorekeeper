package io.vladyslavvua.shorekeeper.feature.editShore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine
import io.vladyslavvua.shorekeeper.jcef.JcefManager
import io.vladyslavvua.shorekeeper.jcef.JcefSettingsProvider
import io.vladyslavvua.shorekeeper.jcef.ShoreCefRouter
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
import io.vladyslavvua.shorekeeper.room.repo.ShoreRepo
import io.vladyslavvua.shorekeeper.server.SetupKtor
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettingsManager
import io.vladyslavvua.shorekeeper.shore.entity.AuthType
import io.vladyslavvua.shorekeeper.shore.entity.Shore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

@KoinViewModel
class EditShoreViewModel(
    @InjectedParam val shoreId: Long,
    private val shoreRepo: ShoreRepo,
) : ViewModel() {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    lateinit var shoreConfig: Shore
    lateinit var shore: ShoreTable
    lateinit var connection: Connection

    val state: StateFlow<EditShoreState>
        field = MutableStateFlow(EditShoreState())


    init {
        viewModelScope.launch(Dispatchers.Default) {
            val foundShore = shoreRepo.getShoreById(shoreId)
            if (foundShore != null) {
                shore = foundShore
            } else {
                throw Exception("Shore not found")
            }
            val config = File("${shore.path}/shore.json")
            try {
                shoreConfig = json.decodeFromString(config.readText())
            } catch (e: Exception) {
                throw Exception("Shore config not found or malformed")
            }
            processConnection()
        }
    }

    private fun processConnection() {
        when (shoreConfig.database.authType) {
            AuthType.NONE -> {
                state.update {
                    it.copy(isAuthDialogOpen = false)
                }
                connection = DriverManager.getConnection(shoreConfig.database.connectionString)
            }

            AuthType.RUNTIME -> {
                val currentState = state.value
                if (!currentState.isAuthDialogOpen) {
                    connection = DriverManager.getConnection(
                        shoreConfig.database.connectionString,
                        currentState.username,
                        currentState.password
                    )
                }
            }

            AuthType.HARDCODED -> {
                state.update {
                    it.copy(isAuthDialogOpen = false)
                }
                connection = DriverManager.getConnection(
                    shoreConfig.database.connectionString,
                    shoreConfig.database.credentials!!.username,
                    shoreConfig.database.credentials!!.password
                )
            }
        }
    }

    fun onIntent(intent: EditShoreIntent) {
        when (intent) {
            is EditShoreIntent.SetCredentials -> setCredentials(intent.username, intent.password)
            else -> Unit
        }
    }


    private fun setCredentials(username: String, password: String) {
        state.update {
            it.copy(
                isAuthDialogOpen = false,
                username = username,
                password = password,
            )
        }
        processConnection()
    }

}