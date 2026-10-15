package io.vladyslavvua.shorekeeper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import io.vladyslavvua.shorekeeper.feature.editShore.sidebar.EditShoreSidebar
import io.vladyslavvua.shorekeeper.navigation.AppGraph
import io.vladyslavvua.shorekeeper.navigation.NavigationPaths
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun App() {
    val navSavedStateConfig = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclassesOfSealed<NavigationPaths>()
            }
        }
    }

    val backStack = rememberNavBackStack(navSavedStateConfig, NavigationPaths.Welcome)

    MaterialTheme {
        Row {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                AppGraph(backStack)
            }
        }
    }
}

