package io.vladyslavvua.shorekeeper.feature.welcome.deleteShoreDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun DeleteShoreDialog(
    onConfirm: (deleteFiles: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var deleteFiles by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
            Text("Are you sure you want to delete this shore?")
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(interactionSource = null, indication = null){
                deleteFiles = !deleteFiles
            }){
                Checkbox(
                    checked = deleteFiles,
                    onCheckedChange = {
                        deleteFiles = it
                    }
                )
                Text("Delete files too")
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }

                TextButton(onClick = { onConfirm(deleteFiles) }) {
                    Text("Yes")
                }
            }

        }

    }

}