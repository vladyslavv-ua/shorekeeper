package io.vladyslavvua.shorekeeper.feature.editShore.quickActions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.vladyslavvua.shorekeeper.ui.components.AuthDialog

@Composable
fun QuickActionsScreen(viewModel: QuickActionsScreenViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isAuthDialogVisible) {
        AuthDialog(
            onCancel = { viewModel.onIntent(QuickSetupIntent.HideAuthDialog) },
            onConfirm = { username, password ->
                viewModel.onIntent(QuickSetupIntent.PerformMigrationWithCredentials(username, password))
            },
        )
    }

    Row {


        Column {

            Button(onClick = {
                viewModel.onIntent(QuickSetupIntent.PerformMigration)
            }) {
                Text("Perform migration")
            }
            Button(onClick = { viewModel.onIntent(QuickSetupIntent.CreateShorekeeperSettingsTable) }) {
                Text("Create Shorekeeper Settings Table")
            }

        }

        Spacer(Modifier.weight(1f))
        Column {
            Text("Logs")
            TextField(
                value = state.logs.reversed().joinToString("\n"),
                readOnly = true,
                onValueChange = {},
                modifier = Modifier.height(200.dp)
            )
        }
    }
}
