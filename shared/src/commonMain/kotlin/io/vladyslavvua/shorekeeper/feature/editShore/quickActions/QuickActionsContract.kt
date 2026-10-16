package io.vladyslavvua.shorekeeper.feature.editShore.quickActions


data class QuickSetupState(
    val isAuthDialogVisible: Boolean = false,
    val logs:List<String> = emptyList()
)
sealed interface QuickSetupIntent{
    object PerformMigration: QuickSetupIntent
    object HideAuthDialog: QuickSetupIntent
    data class PerformMigrationWithCredentials(val username: String, val pass: String): QuickSetupIntent
    object CreateShorekeeperSettingsTable: QuickSetupIntent
}