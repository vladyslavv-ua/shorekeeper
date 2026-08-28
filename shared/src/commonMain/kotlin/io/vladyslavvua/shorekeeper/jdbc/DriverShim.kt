package io.vladyslavvua.shorekeeper.jdbc

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverPropertyInfo
import java.sql.SQLException
import java.util.*
import java.util.logging.Logger


class DriverShim(private val driver: Driver): Driver {

    @Throws(SQLException::class)
    override fun acceptsURL(u: String?): Boolean {
        return this.driver.acceptsURL(u)
    }

    @Throws(SQLException::class)
    override fun connect(u: String?, p: Properties?): Connection? {
        return this.driver.connect(u, p)
    }

    override fun getMajorVersion(): Int {
        return this.driver.majorVersion
    }

    override fun getMinorVersion(): Int {
        return this.driver.minorVersion
    }

    @Throws(SQLException::class)
    override fun getPropertyInfo(u: String?, p: Properties?): Array<DriverPropertyInfo?>? {
        return this.driver.getPropertyInfo(u, p)
    }

    override fun jdbcCompliant(): Boolean {
        return this.driver.jdbcCompliant()
    }

    override fun getParentLogger(): Logger? {
        return this.driver.parentLogger
    }
}