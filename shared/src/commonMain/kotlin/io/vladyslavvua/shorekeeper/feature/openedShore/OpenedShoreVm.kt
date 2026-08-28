package io.vladyslavvua.shorekeeper.feature.openedShore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.sql.Connection
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import java.io.File
import java.sql.DriverManager

@KoinViewModel
class OpenedShoreVm(
    @InjectedParam val shoreId: Long,
    private val shoreRepo: ShoreRepo,
    private val settingsManager: ShorekeeperSettingsManager,
    private val cefSettingsProvider: JcefSettingsProvider,
    private val cefManager: JcefManager,
) : ViewModel() {
    private lateinit var ktorServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
    private var cefClient: CefClient? = null

    @Volatile
    var browser: CefBrowser? = null
    private val ktorConfig = SetupKtor()

    private val _events = Channel<OpenedShoreEffect>()
    val events = _events.receiveAsFlow()
    val state: StateFlow<OpenedShoreState>
        field = MutableStateFlow(OpenedShoreState())

    private lateinit var shoreConfig: Shore
    private lateinit var shore: ShoreTable

    init {
        viewModelScope.launch {
            shore = shoreRepo.getShoreById(shoreId) ?: return@launch

            shoreConfig = Json.decodeFromString<Shore>(File("${shore.path}/shore.json").readText())

            when (shoreConfig.database.authType) {
                AuthType.RUNTIME -> {
                    state.update { it.copy(isAuthDialogOpen = true) }
                }

                AuthType.HARDCODED -> {
                    val connection = DriverManager.getConnection(
                        shoreConfig.database.connectionString,
                        shoreConfig.database.credentials!!.username,
                        shoreConfig.database.credentials!!.password
                    )
                    initKtor(shore)
                    initCef(connection)

                }

                else -> {
                    val connection = DriverManager.getConnection(shoreConfig.database.connectionString)
                    initKtor(shore)
                    initCef(connection)

                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        closeShore()
        println("Cleared")

    }

    private fun initKtor(shore: ShoreTable) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ktorServer = ktorConfig.setup(shore.path).also {
                    it.start(wait = false)
                }
            }
        }
    }

    private fun initCef(connection: Connection) {
        viewModelScope.launch {
            val queryRouter = ShoreCefRouter(
                connection,
                "${shore.path}/queries/"
            )

            val cefSettings = settingsManager.getCefSettings()
            if (cefSettings.isCefSettingsValid()) {
                cefSettingsProvider.provide(cefSettings)
                cefManager.initCefApp()
                cefClient = cefManager.buildJcefClient(listOf(queryRouter))
                browser = cefClient!!.createBrowser("127.0.0.1:3566", false, false)

                state.update { it.copy(isCefReady = true) }
            }
        }
    }

    fun onIntent(intent: OpenedShoreIntent) {
        when (intent) {
            is OpenedShoreIntent.Close -> closeShoreIntent()
            is OpenedShoreIntent.ConfirmCredentials -> tryToConnect(intent.username, intent.password)
            else -> Unit
        }

    }


    fun tryToConnect(username: String, password: String) {
        val connection = DriverManager.getConnection(shoreConfig.database.connectionString, username, password)
        if (connection.isValid(1)) {
            state.update { it.copy(isAuthDialogOpen = false) }
            initKtor(shore)
            initCef(connection)

        }

    }

    private fun closeShore() {

        state.update { it.copy(isCefReady = false) }

        if (this::ktorServer.isInitialized)
            ktorServer.stop(0, 0)
        browser?.close(true)
        cefClient?.dispose()
    }

    private fun closeShoreIntent() {
        state.update { it.copy(isCefReady = false) }

        _events.trySend(OpenedShoreEffect.CloseEffect)
    }


}