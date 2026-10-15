package io.vladyslavvua.shorekeeper.feature.editShore.dto

import io.vladyslavvua.shorekeeper.navigation.EditShore
import org.jetbrains.compose.resources.DrawableResource

data class TabElement(
    val name: String,
    val icon: DrawableResource,
    val destination: EditShore.Tab
)
