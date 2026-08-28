package io.vladyslavvua.shorekeeper.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { effect ->
            when (effect) {
                is SettingsScreenEffect.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Column {
        Text("Jcef path")
        TextField(value = state.cefPath, onValueChange = { viewModel.onIntent(SettingsScreenIntent.SetCefPath(it)) })
        Text("Jcef helper(.exe) path")
        TextField(
            value = state.cefHelperPath,
            onValueChange = { viewModel.onIntent(SettingsScreenIntent.SetCefHelperPath(it)) })
        Text("Jcef cache path")
        TextField(
            value = state.cefCachePath,
            onValueChange = { viewModel.onIntent(SettingsScreenIntent.SetCefCachePath(it)) })



        Button(onClick = { viewModel.onIntent(SettingsScreenIntent.Save) }) {
            Text("Save")
        }
        Button(onClick = {
            viewModel.onIntent(SettingsScreenIntent.NavigateBack)
        }) {
            Text("Back")
        }

    }
}