package io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.vladyslavvua.shorekeeper.shore.entity.AuthType
import io.vladyslavvua.shorekeeper.shore.entity.Migrator

@Composable
fun CreateShoreDialog(onCreate: (CreateShoreDialogState) -> Unit, onCancel: () -> Unit) {
    var shoreName by remember { mutableStateOf("") }
    var shorePath by remember { mutableStateOf("") }
    var shoreMigrator by remember { mutableStateOf(Migrator.LIQUIBASE) }
    var authType by remember { mutableStateOf(AuthType.RUNTIME) }
    var shoreDbConnection by remember { mutableStateOf("") }
    var dbUser by remember { mutableStateOf("") }
    var dbPassword by remember { mutableStateOf("") }

    val launcher = rememberDirectoryPickerLauncher(
        onError = { failure ->
            // A valid directory operation could not be completed
        },
        onResult = { directory ->
            if (directory == null) {
                // The user cancelled the picker
            } else {
                shorePath = directory.absolutePath() + "/" + shoreName
            }
        },
    )

    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .height(500.dp)
                .verticalScroll(rememberScrollState())
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(shoreName, onValueChange = { shoreName = it }, placeholder = { Text("Shore name") })
            Row {
                TextField(shorePath, onValueChange = { shorePath = it }, placeholder = { Text("Shore path") })
                Button(onClick = { launcher.launch() }) {
                    Text("Select Shore Path")
                }
            }
//            TextField(shoreMigrator.name, onValueChange = { shoreMigrator = Migrator.valueOf(it) }, placeholder = { Text("Shore migrator") })
//            TextField(authDb.name, onValueChange = { authDb = AuthDb.valueOf(it) }, placeholder = { Text("Auth db") })
            TextField(
                shoreDbConnection,
                onValueChange = { shoreDbConnection = it },
                placeholder = { Text("Shore db connection") })

            Text("Auth type")
            Column() {

                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(
                        onClick = { authType = AuthType.NONE },
                        indication = null,
                        interactionSource = null
                    )
                ) {
                    RadioButton(
                        selected = authType == AuthType.NONE,
                        onClick = { authType = AuthType.NONE }
                    )
                    Text("None (for databases which don't support authentication e.g. SQLite)")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(
                        onClick = { authType = AuthType.RUNTIME },
                        indication = null,
                        interactionSource = null
                    )
                ) {
                    RadioButton(
                        selected = authType == AuthType.RUNTIME,
                        onClick = { authType = AuthType.RUNTIME }
                    )
                    Text("Runtime (Ask password before connecting)")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(
                        onClick = { authType = AuthType.HARDCODED },
                        indication = null,
                        interactionSource = null
                    )
                ) {
                    RadioButton(
                        selected = authType == AuthType.HARDCODED,
                        onClick = { authType = AuthType.HARDCODED }
                    )
                    Text("Hardcoded (recommended for exceptional cases only)")
                }
            }
            TextField(dbUser, onValueChange = { dbUser = it }, placeholder = { Text("Db user ") })
            TextField(dbPassword, onValueChange = { dbPassword = it }, placeholder = { Text("Db password") })


            Row {

                Button(onClick = onCancel) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        onCreate(
                            CreateShoreDialogState(
                                name = shoreName,
                                path = shorePath,
                                migrator = shoreMigrator,
                                connectionString = shoreDbConnection,
                                authType = authType,
                                dbUser = dbUser,
                                dbPassword = dbPassword
                            )
                        )
                    }
                ) {
                    Text("Create")
                }

            }
        }
    }

}