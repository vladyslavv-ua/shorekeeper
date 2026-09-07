package io.vladyslavvua.shorekeeper.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexBoxConfig
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog.CreateShoreDialog
import io.vladyslavvua.shorekeeper.navigation.NavigationPaths
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalFlexBoxApi::class)
@Composable
fun WelcomeScreen(
    navGraph: NavController,
) {
    val viewModel = koinViewModel<WelcomeScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isCreateShoreDialogOpen) {
        CreateShoreDialog({
            viewModel.onIntent(WelcomeIntent.CreateShore(it))
        }, { viewModel.onIntent(WelcomeIntent.CloseCreateShoreDialog) })
    }


    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { effect ->
            when (effect) {
                is WelcomeEffect.OpenSettingsAction -> navGraph.navigate(NavigationPaths.Settings)
                is WelcomeEffect.OpenShore -> navGraph.navigate(NavigationPaths.Welcome.Shore(effect.shoreId))
                is WelcomeEffect.OpenCef -> navGraph.navigate(NavigationPaths.OpenCef)
                is WelcomeEffect.OpenEditShore -> navGraph.navigate(NavigationPaths.Welcome.EditShore(effect.shoreId))
                else -> Unit
            }
        }
    }

    Row(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Red)) {
        Column {

            Text("Available shores:")

            LazyColumn(Modifier.height(500.dp).background(Color.White)) {
                items(state.shores, key = { it.id }) {
                    Text(
                        it.name, modifier = Modifier.width(200.dp).clickable(onClick = {
                            viewModel.onIntent(WelcomeIntent.SelectShore(it.id))
                        }).background(if (it.selected) Color.Black else Color.Transparent),
                        color = if (it.selected) Color.White else Color.Black
                    )
                }
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        Column {

            Button(onClick = {
                viewModel.onIntent(WelcomeIntent.OpenShore)
            }) {
                Text("Launch")
            }

            Button(onClick = {
                viewModel.onIntent(WelcomeIntent.OpenCreateShoreDialog)
            }) {
                Text("New shore")
            }
            Button(onClick = {
                viewModel.onIntent(WelcomeIntent.OpenAddShoreDialog)
            }) {
                Text("Add shore")
            }

            Button(onClick = {
                viewModel.onIntent(WelcomeIntent.OpenEditShore)
            }){
                Text("Edit shore")
            }

            Button(
                onClick = {
                    viewModel.onIntent(WelcomeIntent.OpenCef)
                }
            ){
                Text("Cef")
            }
            Button(onClick = {
                viewModel.onIntent(WelcomeIntent.OpenSettings)
            }) {
                Text("Settings")
            }
        }

    }

}