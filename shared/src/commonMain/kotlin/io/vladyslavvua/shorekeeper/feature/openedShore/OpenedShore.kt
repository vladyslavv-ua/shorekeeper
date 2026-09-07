package io.vladyslavvua.shorekeeper.feature.openedShore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.vladyslavvua.shorekeeper.feature.openedShore.OpenedShoreIntent.ConfirmCredentials
import io.vladyslavvua.shorekeeper.ui.components.AuthDialog
import io.vladyslavvua.shorekeeper.ui.components.BrowserView
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun OpenedShore(shoreId: Long, navController: NavController) {
    val viewModel: OpenedShoreVm = koinViewModel { parametersOf(shoreId) }
    val browser = viewModel.browser
    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.events.collect { effect ->

            when (effect) {
                is OpenedShoreEffect.CloseEffect -> {
                    navController.navigateUp()

                }

                else -> Unit
            }
        }
    }

    if (state.isAuthDialogOpen) {
        AuthDialog(
            onCancel = {
                viewModel.onIntent(OpenedShoreIntent.Close)

            },
            onConfirm = { username, password ->
                viewModel.onIntent(
                    ConfirmCredentials(
                        username = username,
                        password = password
                    )
                )
            })
    }

    Column {
        Row {
            Button(
                onClick = { viewModel.onIntent(OpenedShoreIntent.Close) }
            ) {
                Text("Close")
            }
        }

        Row {

            if (state.isCefReady) {

                BrowserView(browser!!)


            }

        }
    }
}
