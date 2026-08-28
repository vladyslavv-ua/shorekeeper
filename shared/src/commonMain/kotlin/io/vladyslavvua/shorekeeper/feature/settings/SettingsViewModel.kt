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
                    cefCachePath = settingsManager.getCefSettings().cefCachePath
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

            else -> Unit
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
        viewModelScope.launch {
            settingsManager.updateSettings(
                ShorekeeperSettings(
                    settings.cefPath,
                    settings.cefHelperPath,
                    settings.cefCachePath
                )
            )
        }
    }
}