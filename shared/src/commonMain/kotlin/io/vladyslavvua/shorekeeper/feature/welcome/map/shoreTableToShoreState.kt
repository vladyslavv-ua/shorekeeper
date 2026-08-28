package io.vladyslavvua.shorekeeper.feature.welcome.map

import io.vladyslavvua.shorekeeper.feature.welcome.WelcomeState
import io.vladyslavvua.shorekeeper.room.entity.ShoreTable

fun ShoreTable.toShoreState() = WelcomeState.ShoreState(
    id = id ?: 0,
    name = name,
    path = path,
)