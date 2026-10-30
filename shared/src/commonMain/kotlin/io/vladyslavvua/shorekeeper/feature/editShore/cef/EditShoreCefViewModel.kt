package io.vladyslavvua.shorekeeper.feature.editShore.cef

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine
import io.vladyslavvua.shorekeeper.feature.editShore.EditShoreIntent
import io.vladyslavvua.shorekeeper.feature.editShore.EditShoreState
import io.vladyslavvua.shorekeeper.feature.editShore.dto.DbCredentials
import io.vladyslavvua.shorekeeper.jcef.JcefManager
import io.vladyslavvua.shorekeeper.jcef.JcefSettingsProvider
import io.vladyslavvua.shorekeeper.jcef.ShoreCefRouter
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
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
class EditShoreCefViewModel(
    @InjectedParam
    private val shore: ShoreTable,
    @InjectedParam
    private val shoreConfig: Shore,
    @InjectedParam
    private val connection: Connection,

    private val settingsManager: ShorekeeperSettingsManager,
    private val cefSettingsProvider: JcefSettingsProvider,
    private val cefManager: JcefManager,
) : ViewModel() {
    private lateinit var ktorServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
    private var cefClient: CefClient? = null

    @Volatile
    var browser: CefBrowser? = null
    private val ktorConfig = SetupKtor()

    val state: StateFlow<EditShoreCefState>
        field = MutableStateFlow(EditShoreCefState())


    fun onIntent(intent: EditShoreCefIntent) {
        when (intent) {
            is EditShoreCefIntent.SetUrl -> setUrl(intent.url)
            is EditShoreCefIntent.RunCef -> runCef()
            is EditShoreCefIntent.ToggleDevTools -> toggleDevTools()
            else -> Unit
        }
    }

    private fun setUrl(url: String) {
        state.update {
            it.copy(url = url)
        }

    }

    private fun toggleDevTools() {
        val browser = browser ?: return
        if (state.value.isDevToolsOpen) {
            browser.closeDevTools()
        } else {
            browser.openDevTools()
        }
        state.update {
            it.copy(isDevToolsOpen = !it.isDevToolsOpen)
        }
    }

    private fun runCef() {
        viewModelScope.launch(Dispatchers.IO) {
            initKtor(shore)
            initCef(connection)

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


    private suspend fun initKtor(shore: ShoreTable) {
        ktorServer = ktorConfig.setup(shore.path).also {
            it.start(wait = false)

        }
    }

}