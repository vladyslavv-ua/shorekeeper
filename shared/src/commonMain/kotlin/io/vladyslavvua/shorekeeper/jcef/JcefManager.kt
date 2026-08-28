package io.vladyslavvua.shorekeeper.jcef

import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.friwi.jcefmaven.CefAppBuilder
import org.cef.CefApp
import org.cef.CefClient
import org.cef.handler.CefMessageRouterHandlerAdapter
import java.io.File


class JcefManager(
    private val settingsProvider: JcefSettingsProvider
) {
    @Volatile
    private var cefApp: CefApp? = null
    private val initMutex = Mutex()

    suspend fun initCefApp(): CefApp {
        cefApp?.let { return it }

        return initMutex.withLock {
            cefApp ?: run {
                val settings = settingsProvider.awaitSettings()

                val builder = CefAppBuilder()
                builder.setInstallDir(File(settings.cefPath))
                builder.skipInstallation = true
                builder.cefSettings.apply {
                    windowless_rendering_enabled = false
                    browser_subprocess_path = settings.cefHelper
                    isDebugInspectorInfoEnabled = true
                    root_cache_path = File(settings.cefCachePath).absolutePath
                    remote_debugging_port = 9222
                }

                builder.addJcefArgs("--remote-debugging-port=9222")

                builder.addJcefArgs("--remote-allow-origins=*")
                builder.build().also { cefApp = it }
            }
        }
    }

    // якщо десь потрібен non-suspend доступ, коли singleton вже точно ініціалізований
    fun getCefAppOrNull(): CefApp? = cefApp


    fun buildJcefClient(customMessageRouters: List<CefMessageRouterHandlerAdapter> = emptyList()): CefClient? {
        if (cefApp == null) return null
        val client = cefApp!!.createClient()
        val basicRouterHandler = BasicRouterHandler()
        val router = basicRouterHandler.router
        customMessageRouters.forEach {
            router.addHandler(it, false)
        }
//        customMessageRouters.forEach { client.addMessageRouter(it) }
        client.addMessageRouter(router)
        return client
    }
}