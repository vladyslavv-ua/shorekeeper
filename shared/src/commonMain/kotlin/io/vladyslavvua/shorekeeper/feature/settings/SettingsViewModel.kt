package io.vladyslavvua.shorekeeper.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettings
import io.vladyslavvua.shorekeeper.settings.ShorekeeperSettingsManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import java.io.File

@KoinViewModel
class SettingsViewModel(
    val settingsManager: ShorekeeperSettingsManager
) : ViewModel() {
    val state: StateFlow<SettingsScreenState>
        field = MutableStateFlow(SettingsScreenState())

    private val _events = Channel<SettingsScreenEffect>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            state.update {
                it.copy(
                    cefPath = settingsManager.getCefSettings().cefPath,
                    cefHelperPath = settingsManager.getCefSettings().cefHelper,
                    cefCachePath = settingsManager.getCefSettings().cefCachePath,
                    liquibasePath = settingsManager.getSettings().liquibasePath,
                )
            }
        }
    }

    fun onIntent(intent: SettingsScreenIntent) {
        when (intent) {
            is SettingsScreenIntent.SetCefPath -> updateCefField(intent.path)
            is SettingsScreenIntent.SetCefHelperPath -> updateCefHelperField(intent.path)
            is SettingsScreenIntent.SetCefCachePath -> updateCefCacheField(intent.path)
            is SettingsScreenIntent.Save -> saveSettings()
            is SettingsScreenIntent.NavigateBack -> navigateBack()
            is SettingsScreenIntent.SetLiquibasePath -> updateLiquibaseField(intent.path)
            is SettingsScreenIntent.CheckLiquibasePath -> checkLiquibasePath()

            else -> Unit
        }
    }

    private fun checkLiquibasePath() {
        val path = state.value.liquibasePath
        if (File(path).exists()) {
            val process = ProcessBuilder()
                .command(path, "--version")
                .start()
            process.waitFor()
            if (process.exitValue() == 0) {
                state.update {
                    it.copy(liquibasePathValid = true)
                }
            }
            else if (path == ""){
                state.update {
                    it.copy(liquibasePathValid = null)
                }
            }
            else {
                state.update {
                    it.copy(liquibasePathValid = false)
                }
            }
        }
    }

    private fun updateLiquibaseField(path: String) {
        state.update {
            it.copy(liquibasePath = path)
        }
    }

    private fun updateCefField(path: String) {
        state.update {
            it.copy(cefPath = path)
        }
    }

    private fun updateCefHelperField(path: String) {
        state.update {
            it.copy(cefHelperPath = path)
        }
    }

    private fun updateCefCacheField(path: String) {
        state.update {
            it.copy(cefCachePath = path)
        }
    }

    private fun navigateBack() {
        _events.trySend(SettingsScreenEffect.NavigateBack)
    }

    private fun saveSettings() {
        val settings = state.value
        checkLiquibasePath()
        viewModelScope.launch {
            settingsManager.updateSettings(
                ShorekeeperSettings(
                    settings.cefPath,
                    settings.cefHelperPath,
                    settings.cefCachePath,
                    liquibasePath = if (settings.liquibasePathValid == true) settings.liquibasePath else ""
                )
            )
        }
    }
}