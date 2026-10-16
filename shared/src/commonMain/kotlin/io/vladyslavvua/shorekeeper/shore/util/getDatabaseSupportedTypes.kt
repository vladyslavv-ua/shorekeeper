package io.vladyslavvua.shorekeeper.shore.util

import io.vladyslavvua.shorekeeper.shore.entity.DatabaseType
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.SQLException


@Throws(SQLException::class)
fun getSupportedTypes(connection: Connection): List<DatabaseType> {
    val metaData: DatabaseMetaData = connection.metaData
    val supportedTypes = mutableListOf<DatabaseType>()

    metaData.typeInfo.use { rs ->
        println("Supported types in the database:")
        while (rs.next()) {
            val typeName = rs.getString("TYPE_NAME")
            val javaSqlType = rs.getInt("DATA_TYPE")
            val isNullable = rs.getShort("NULLABLE").toInt() != DatabaseMetaData.typeNoNulls
            supportedTypes.add(DatabaseType(typeName, javaSqlType, isNullable))
//            println(
//                "Name: $typeName | JDBC Type Code: $javaSqlType | Nullable: $isNullable"
//            )
        }
    }

    return supportedTypes.toList()
}