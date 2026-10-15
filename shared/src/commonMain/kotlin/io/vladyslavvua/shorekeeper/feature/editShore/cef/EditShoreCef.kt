package io.vladyslavvua.shorekeeper.feature.editShore.cef

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.vladyslavvua.shorekeeper.feature.editShore.EditShoreIntent
import io.vladyslavvua.shorekeeper.ui.components.BrowserView

@Composable
fun EditShoreCef(viewModel: EditShoreCefViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column {

        TextField(
            value = state.url,
            onValueChange = { viewModel.onIntent(EditShoreCefIntent.SetUrl(it)) })
        Button(onClick = { viewModel.onIntent(EditShoreCefIntent.RunCef) }) {
            Text("Run")
        }



        if (state.isCefReady) {
            BrowserView(viewModel.browser!!)
        }
    }
}
