package io.vladyslavvua.shorekeeper.feature.editShore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.vladyslavvua.shorekeeper.feature.editShore.cef.EditShoreCef
import io.vladyslavvua.shorekeeper.feature.editShore.cef.EditShoreCefIntent
import io.vladyslavvua.shorekeeper.feature.editShore.cef.EditShoreCefViewModel
import io.vladyslavvua.shorekeeper.feature.editShore.dto.TabElement
import io.vladyslavvua.shorekeeper.feature.editShore.sidebar.EditShoreSidebar
import io.vladyslavvua.shorekeeper.navigation.EditShore
import io.vladyslavvua.shorekeeper.ui.components.AuthDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import shorekeeper.shared.generated.resources.Res
import shorekeeper.shared.generated.resources.chrome
import shorekeeper.shared.generated.resources.magic_wand
import kotlin.collections.associateWith

@Composable
fun EditShoreScreen(shoreId: Long) {
    // todo move to separate subscreen
    val viewModel: EditShoreViewModel = koinViewModel(parameters = { parametersOf(shoreId) })
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tabs = listOf(
        TabElement("Quick actions", Res.drawable.magic_wand, EditShore.Tab.QuickSetup),
        TabElement("Cef", Res.drawable.chrome, EditShore.Tab.Cef),
    )
    val backStacks = remember {
        tabs.associateWith { tab -> NavBackStack<NavKey>(tab.destination) }
    }

    val saveableStateHolder = rememberSaveableStateHolder()

    var selectedTab by rememberSaveable { mutableStateOf<TabElement>(tabs.first()) }

    if (state.isAuthDialogOpen) {
        AuthDialog(
            onCancel = {

            },
            onConfirm = { username, password ->
                viewModel.onIntent(EditShoreIntent.SetCredentials(username, password))

            }
        )
    }




    Row(Modifier.fillMaxSize()) {
        EditShoreSidebar(
            tabs = tabs,
            selectedTab = selectedTab.destination,
            navigate = { destination ->
                selectedTab = tabs.first {
                    it.destination == destination
                }
            })
        Column(modifier = Modifier.weight(1f)) {
            NavDisplay(
                backStack = backStacks[selectedTab]!!,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<EditShore.Tab.QuickSetup> {
                        Text("Quick Setup")
                    }
                    entry<EditShore.Tab.Cef> {
                        val viewModel: EditShoreCefViewModel = koinViewModel(
                            parameters = {
                                parametersOf(
                                    viewModel.shore,
                                    viewModel.shoreConfig,
                                    viewModel.connection
                                )
                            }
                        )
                        EditShoreCef(viewModel)
                    }
                }
            )

        }
    }
}
