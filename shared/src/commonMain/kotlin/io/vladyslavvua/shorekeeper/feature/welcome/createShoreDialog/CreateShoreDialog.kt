package io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
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

    Dialog(onDismissRequest = onCancel) {
        Column {

            TextField(shoreName, onValueChange = { shoreName = it }, placeholder = { Text("Shore name") })
            TextField(shorePath, onValueChange = { shorePath = it }, placeholder = { Text("Shore path") })
//            TextField(shoreMigrator.name, onValueChange = { shoreMigrator = Migrator.valueOf(it) }, placeholder = { Text("Shore migrator") })
//            TextField(authDb.name, onValueChange = { authDb = AuthDb.valueOf(it) }, placeholder = { Text("Auth db") })
            TextField(shoreDbConnection, onValueChange = { shoreDbConnection = it }, placeholder = { Text("Shore db connection") })
            TextField(dbUser, onValueChange = { dbUser = it }, placeholder = { Text("Db user ") })
            TextField(dbPassword, onValueChange = { dbPassword = it }, placeholder = { Text("Db password")})


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