package io.vladyslavvua.shorekeeper.feature.openedCef

data class OpenedCefState(
    val cefUrl: String = "localhost:8080",
    val isCefReady: Boolean = false
)

sealed class OpenedCefIntent{
    data class SetCefUrl(val url: String) : OpenedCefIntent()
    data object StartCef : OpenedCefIntent()
}