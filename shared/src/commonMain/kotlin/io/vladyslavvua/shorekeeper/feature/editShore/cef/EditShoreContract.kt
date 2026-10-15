package io.vladyslavvua.shorekeeper.feature.editShore.cef

import io.vladyslavvua.shorekeeper.feature.editShore.EditShoreIntent

data class EditShoreCefState(
    val url: String = "localhost:4200",
    val isCefReady: Boolean = false,
)

sealed class EditShoreCefIntent{
    data class SetUrl(val url: String) : EditShoreCefIntent()
    data object RunCef : EditShoreCefIntent()
}