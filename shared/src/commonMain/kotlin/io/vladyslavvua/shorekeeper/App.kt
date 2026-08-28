package io.vladyslavvua.shorekeeper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.vladyslavvua.shorekeeper.navigation.AppGraph


@Composable
@Preview
fun App() {
    MaterialTheme {
//        val client = CefManager.cefApp.createClient()
//        val browser = client.createBrowser("https://youtube.com/", false, false)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            AppGraph()
        }
    }
}

