package io.vladyslavvua.shorekeeper.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.vladyslavvua.shorekeeper.feature.about.AboutScreen
import io.vladyslavvua.shorekeeper.feature.editShore.EditShoreScreen
import io.vladyslavvua.shorekeeper.feature.openedCef.OpenedCefScreen
import io.vladyslavvua.shorekeeper.feature.openedShore.OpenedShoreScreen
import io.vladyslavvua.shorekeeper.feature.settings.SettingsScreen
import io.vladyslavvua.shorekeeper.feature.welcome.WelcomeScreen
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun AppGraph() {

    val navSavedStateConfig = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclassesOfSealed<NavigationPaths>()
            }
        }
    }

    val backStack = rememberNavBackStack(navSavedStateConfig, NavigationPaths.Welcome)


    NavDisplay(
        backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider{
            entry<NavigationPaths.Welcome> {
                WelcomeScreen(
                    onNavigate = { navPath ->
                        backStack.add(navPath)
                    }
                )
            }
            entry<NavigationPaths.About> {
                AboutScreen(onBackClick = backStack::removeLastOrNull)
            }
            entry<NavigationPaths.Shore.OpenedShore> { entry ->
                OpenedShoreScreen(shoreId = entry.shoreId, backStack::removeLastOrNull)
            }
            entry<NavigationPaths.Shore.EditShore> { entry ->
                EditShoreScreen(shoreId = entry.shoreId)
            }
            entry<NavigationPaths.OpenCef> {
                OpenedCefScreen()
            }
            entry<NavigationPaths.Settings> {
                SettingsScreen(onNavigateBack = backStack::removeLastOrNull)
            }
        }
    )
}