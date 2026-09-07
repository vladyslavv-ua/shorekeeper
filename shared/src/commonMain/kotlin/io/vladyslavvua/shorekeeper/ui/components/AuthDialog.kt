package io.vladyslavvua.shorekeeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AuthDialog(
    onCancel: () -> Unit,
    onConfirm: (username: String, password: String) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onCancel) {

        Column(modifier = Modifier.background(color = Color.White).padding(16.dp)) {
            Text("Enter your credentials")

            Text("Username")
            TextField(
                value = username,
                onValueChange = { username = it }
            )
            Text("Password")
            TextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )


            Row {
                Button(
                    onClick = {
                        onCancel()
                    }
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        onConfirm(username, password)
                    }
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}