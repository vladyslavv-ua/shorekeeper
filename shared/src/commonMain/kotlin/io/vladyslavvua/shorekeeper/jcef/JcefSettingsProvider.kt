package io.vladyslavvua.shorekeeper.jcef

import kotlinx.coroutines.CompletableDeferred

class JcefSettingsProvider {
    private val deferred = CompletableDeferred<ShoreKeeperCefSettings>()

    suspend fun awaitSettings(): ShoreKeeperCefSettings = deferred.await()

    fun provide(settings: ShoreKeeperCefSettings) {
        if (!deferred.isCompleted) {
            deferred.complete(settings)
        }
    }
}