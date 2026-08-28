package io.vladyslavvua.shorekeeper.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import org.cef.browser.CefBrowser

@Composable
fun BrowserView(browser: CefBrowser, modifier: Modifier = Modifier) {
    SwingPanel(
        modifier = modifier.fillMaxSize(),
        factory = { browser.uiComponent as java.awt.Component }
    )
}