package io.vladyslavvua.shorekeeper.migrator.liquibase

import io.vladyslavvua.shorekeeper.migrator.ShorekeeperMigrator
import java.io.File


class LiquibaseMigrator(private val cliPath: String) : ShorekeeperMigrator {
    override fun initProject(projectPath: String, jdbcUrl: String, user: String, password: String): ProcessBuilder {
        return ProcessBuilder(
            listOf(
                cliPath, "init", "project",
                "--project-guide", "off",
                "--project-dir", projectPath,
                "--changelog-file", "changelog-master.yaml",
                "--url", jdbcUrl,
                "--username", user,
                "--password", password
            )
        )
    }

    override fun initFileStructure(projectPath: String) {
        val project = File("$projectPath/migrations/changelogs")
        project.mkdirs()
        File("$projectPath/migrations/changelog-master.yaml").createNewFile()
    }

    override fun migrate(projectPath: String, jdbcUrl: String, user: String?, password: String?): ProcessBuilder {
        if (user == null || password == null) {
            return ProcessBuilder(
                listOf(
                    cliPath,
                    "--search-path", "$projectPath/migrations",
                    "--defaults-file", "$projectPath/migrations/liquibase.properties",
                    "update"
                )
            )
        } else {
            return ProcessBuilder(
                listOf(
                    cliPath,
                    "--search-path", "$projectPath/migrations",
                    "--defaults-file", "$projectPath/migrations/liquibase.properties",
                    "update",
                    "--username", user,
                    "--password", password
                )
            )
        }
    }
}