package io.vladyslavvua.shorekeeper.feature.welcome.addShoreDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import shorekeeper.shared.generated.resources.Res
import shorekeeper.shared.generated.resources.folder_open


@Composable
fun AddShoreDialog(
    onConfirm: (name: String, path: String) -> Unit,
    onDismiss: () -> Unit
) {
    var shoreName by remember { mutableStateOf("") }
    var shorePath by remember { mutableStateOf("") }
    val shorePathLauncher = rememberDirectoryPickerLauncher(
        onResult = { shorePath = it!!.absolutePath() }
    )
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.background(Color.White).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = shoreName,
                onValueChange = { shoreName = it },
                placeholder = { Text("Shore name") }
            )
            TextField(
                value = shorePath,
                onValueChange = {},
                maxLines = 1,
                placeholder = { Text("Shore path") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            shorePathLauncher.launch()
                        },
                        interactionSource = null,

                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.folder_open),
                            contentDescription = null
                        )
                    }
                }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
                TextButton(onClick = { onConfirm(shoreName, shorePath) }) {
                    Text("Add")
                }
            }
        }
    }
}