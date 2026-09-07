package io.vladyslavvua.shorekeeper.feature.editShore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.vladyslavvua.shorekeeper.feature.openedShore.OpenedShoreIntent
import io.vladyslavvua.shorekeeper.ui.components.AuthDialog
import io.vladyslavvua.shorekeeper.ui.components.BrowserView
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditShoreScreen(shoreId: Long, navController: NavController) {
    // todo move to separate subscreen
    val viewModel: EditShoreViewModel = koinViewModel(parameters = { parametersOf(shoreId) })
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isAuthDialogOpen) {
        AuthDialog(
            onCancel = {

            },
            onConfirm = { username, password ->
                viewModel.onIntent(EditShoreIntent.SetCredentials(username, password))

            }
        )
    }
    Column {
        Row {


            TextField(value = state.url, onValueChange = { viewModel.onIntent(EditShoreIntent.SetUrl(it)) })
            Button(onClick = { viewModel.onIntent(EditShoreIntent.RunCef) }) {
                Text("Run")
            }
        }


        if (state.isCefReady) {
            BrowserView(viewModel.browser!!)
        }
    }
}