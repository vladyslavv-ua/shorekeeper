package io.vladyslavvua.shorekeeper.feature.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Text("About")
        Text("Version: ??????????")
        Text("Author: vladyslavvua")
        Text("License: MIT")

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

