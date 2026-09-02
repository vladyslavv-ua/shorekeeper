package io.vladyslavvua.shorekeeper.feature.openedCef

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.vladyslavvua.shorekeeper.ui.components.BrowserView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OpenedCefScreen() {
    val viewModel: OpenedCefVewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        Row {
            TextField(state.cefUrl, onValueChange = { viewModel.onIntent(OpenedCefIntent.SetCefUrl(it)) })
            Button(
                onClick = {
                    viewModel.onIntent(OpenedCefIntent.StartCef)
                }
            ) {
                Text("Run")
            }
        }

        if (state.isCefReady) {
            BrowserView(viewModel.browser!!)
        }
    }
}