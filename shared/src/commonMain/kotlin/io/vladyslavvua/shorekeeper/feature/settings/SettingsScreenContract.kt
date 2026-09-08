package io.vladyslavvua.shorekeeper.feature.settings

data class SettingsScreenState(
    val cefPath: String = "",
    val cefHelperPath: String = "",
    val cefCachePath: String = "",
    val liquibasePath: String = "",
    val liquibasePathValid: Boolean? = null
)

sealed class SettingsScreenIntent {
    data class SetCefPath(val path: String) : SettingsScreenIntent()
    data class SetCefHelperPath(val path: String) : SettingsScreenIntent()
    data class SetCefCachePath(val path: String) : SettingsScreenIntent()
    data class SetLiquibasePath(val path: String) : SettingsScreenIntent()
    data object CheckLiquibasePath : SettingsScreenIntent()
    data object Save : SettingsScreenIntent()
    data object NavigateBack : SettingsScreenIntent()
}

sealed class SettingsScreenEffect {
    data object NavigateBack : SettingsScreenEffect()

}