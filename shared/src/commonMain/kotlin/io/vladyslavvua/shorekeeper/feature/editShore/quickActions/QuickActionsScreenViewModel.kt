package io.vladyslavvua.shorekeeper.feature.editShore.quickActions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettingsManager
import io.vladyslavvua.shorekeeper.shore.entity.AuthType
import io.vladyslavvua.shorekeeper.shore.entity.Shore
import io.vladyslavvua.shorekeeper.shore.util.initShorekeeperSettingsTable
import io.vladyslavvua.shorekeeper.shore.util.printSupportedTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import java.sql.Connection

@KoinViewModel
class QuickActionsScreenViewModel(
    @InjectedParam
    private val shore: ShoreTable,
    @InjectedParam
    private val shoreConfig: Shore,
    @InjectedParam
    private val connection: Connection,

    private val settingsManager: ShorekeeperSettingsManager,
) : ViewModel() {

    val state: StateFlow<QuickSetupState>
        field = MutableStateFlow(QuickSetupState())

    fun onIntent(intent: QuickSetupIntent) {
        when (intent) {
            is QuickSetupIntent.PerformMigration -> performMigrationAction()
            is QuickSetupIntent.PerformMigrationWithCredentials -> performMigration(intent.username, intent.pass)
            is QuickSetupIntent.HideAuthDialog -> hideAuthDialog()
            is QuickSetupIntent.CreateShorekeeperSettingsTable -> createShorekeeperSettingsTable()

            else -> Unit
        }
    }

    private fun createShorekeeperSettingsTable() {
        viewModelScope.launch(Dispatchers.IO) {
            initShorekeeperSettingsTable(connection)
            state.update { it.copy(logs = it.logs + "Table created") }
        }
    }

    private fun hideAuthDialog() {
        state.update { it.copy(isAuthDialogVisible = false) }
    }

    private fun performMigrationAction() {
        if (shoreConfig.database.authType == AuthType.RUNTIME) {
            state.update {
                it.copy(isAuthDialogVisible = true)
            }
        } else {
            performMigration(null, null)
        }
    }

    private fun performMigration(username: String?, pass: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val migrator = settingsManager.getMigratorPath(shoreConfig.migrator)
            val process = migrator.migrate(shore.path, shoreConfig.database.connectionString, username, pass)
            process.inheritIO()
            process.start().waitFor()
            hideAuthDialog()
            state.update { it.copy(logs = it.logs + "Migration completed") }
        }
    }
}