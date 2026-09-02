package io.vladyslavvua.shorekeeper.feature.openedCef

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.vladyslavvua.shorekeeper.jcef.JcefManager
import io.vladyslavvua.shorekeeper.jcef.JcefSettingsProvider
import io.vladyslavvua.shorekeeper.jcef.ShoreCefRouter
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.cef.CefClient
import org.koin.core.annotation.KoinViewModel
import java.sql.Connection

@KoinViewModel
class OpenedCefVewModel(
    private val settingsManager: ShorekeeperSettingsManager,
    private val cefSettingsProvider: JcefSettingsProvider,
    private val cefManager: JcefManager,
) : ViewModel() {
    val state: StateFlow<OpenedCefState>
        field = MutableStateFlow(OpenedCefState())

    var cefClient: CefClient? = null
    var browser: org.cef.browser.CefBrowser? = null

    fun onIntent(intent: OpenedCefIntent) {
        when (intent) {
            is OpenedCefIntent.SetCefUrl -> setCefUrl(intent.url)
            is OpenedCefIntent.StartCef -> runCef()
            else -> Unit
        }
    }

    private fun setCefUrl(url: String) {
        state.update {
            it.copy(cefUrl = url)
        }
    }

    private fun runCef() {
        viewModelScope.launch {
            val lastState = state.value

            val cefSettings = settingsManager.getCefSettings()
            if (cefSettings.isCefSettingsValid()) {
                cefSettingsProvider.provide(cefSettings)
                cefManager.initCefApp()
                cefClient = cefManager.buildJcefClient()
                browser = cefClient!!.createBrowser(lastState.cefUrl, false, false)

                state.update { it.copy(isCefReady = true) }
            }

        }


    }
}