package io.vladyslavvua.shorekeeper.feature.welcome.createShoreDialog

import io.vladyslavvua.shorekeeper.APP_VERSION_CODE
import io.vladyslavvua.shorekeeper.shore.entity.AuthType
import io.vladyslavvua.shorekeeper.shore.entity.Migrator
import io.vladyslavvua.shorekeeper.shore.entity.Shore
import io.vladyslavvua.shorekeeper.shore.entity.ShoreDbConnections

data class CreateShoreDialogState(
    val name: String = "",
    val path: String = "",
    val migrator: Migrator,
    val authType: AuthType,
    val connectionString: String,
    val dbUser: String,
    val dbPassword: String
) {
    fun toShore(): Shore = Shore(
        shorekeeperVersion = APP_VERSION_CODE,
        name = name,
        version = 0,
        migrator = migrator,
        database = ShoreDbConnections(
            connectionString = connectionString,
            authType = authType,
            credentials = if (authType == AuthType.HARDCODED) ShoreDbConnections.Credentials(
                dbUser,
                dbPassword
            ) else null
        )
    )
}