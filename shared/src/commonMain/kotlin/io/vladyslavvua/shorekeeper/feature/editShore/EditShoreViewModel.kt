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
    private val settingsManager: ShorekeeperSettingsManager,
    private val cefSettingsProvider: JcefSettingsProvider,
    private val cefManager: JcefManager,
) : ViewModel() {

    private lateinit var ktorServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
    private var cefClient: CefClient? = null

    @Volatile
    var browser: CefBrowser? = null
    private val ktorConfig = SetupKtor()

    private lateinit var shoreConfig: Shore
    private lateinit var shore: ShoreTable

    val state: StateFlow<EditShoreState>
        field = MutableStateFlow(EditShoreState())

    fun onIntent(intent: EditShoreIntent) {
        when (intent) {
            is EditShoreIntent.SetUrl -> setUrl(intent.url)
            is EditShoreIntent.RunCef -> runCef()
            is EditShoreIntent.SetCredentials -> setCredentials(intent.username, intent.password)
            else -> Unit
        }
    }

    private fun setUrl(url: String) {
        state.update {
            it.copy(url = url)
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
    }

    private fun runCef() {
        viewModelScope.launch {
            shore = shoreRepo.getShoreById(shoreId) ?: return@launch

            shoreConfig = Json.decodeFromString<Shore>(File("${shore.path}/shore.json").readText())

            when (shoreConfig.database.authType) {
                AuthType.RUNTIME -> {
                    val connection = DriverManager.getConnection(
                        shoreConfig.database.connectionString,
                        state.value.username,
                        state.value.password
                    )
                    initKtor(shore)
                    initCef(connection)
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
                browser = cefClient!!.createBrowser(state.value.url, false, false)

                state.update { it.copy(isCefReady = true) }
            }
        }
    }

}