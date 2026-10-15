package io.vladyslavvua.shorekeeper.feature.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Text("About", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text("Version: ??????????")
        Text("Author: vladyslavvua")
        Text("License: MIT")

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

