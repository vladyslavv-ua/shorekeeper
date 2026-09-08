package io.vladyslavvua.shorekeeper.migrator

interface ShorekeeperMigrator {
    fun initProject(projectPath:String, jdbcUrl: String, user: String, password: String): ProcessBuilder
    fun initFileStructure(projectPath:String)
}