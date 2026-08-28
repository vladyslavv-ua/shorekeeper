package io.vladyslavvua.shorekeeper.jcef

data class ShoreKeeperCefSettings(
    val cefPath: String,
    val cefHelper: String,
    val cefCachePath: String
) {
    fun isCefSettingsValid(): Boolean = cefPath.isNotEmpty() && cefHelper.isNotEmpty() && cefCachePath.isNotEmpty()

}