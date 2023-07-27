package ru.otus.kotlin.brown.app.ktor.settings

import io.ktor.server.config.*

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

data class DataSourceConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val schema: String
) {
    companion object {
        fun createDataSourceConfig(applicationConfig: ApplicationConfig) = DataSourceConfig(
            applicationConfig.property("ktor.datasource.jdbcUrl").getString(),
            applicationConfig.property("ktor.datasource.username").getString(),
            applicationConfig.property("ktor.datasource.password").getString(),
            applicationConfig.property("ktor.datasource.schema").getString()
        )

        fun createDataSource(dataSourceConfig: DataSourceConfig): HikariDataSource {
            val hikariConfig = HikariConfig()
            hikariConfig.username = dataSourceConfig.username
            hikariConfig.password = dataSourceConfig.password
            hikariConfig.jdbcUrl = dataSourceConfig.jdbcUrl
            hikariConfig.schema = dataSourceConfig.schema
            hikariConfig.maximumPoolSize = 10

            return HikariDataSource(hikariConfig)
        }

        fun flywayMigrate(dataSource: DataSource, dataSourceConfig: DataSourceConfig) {
            val flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(dataSourceConfig.schema)
                .load()

            flyway.migrate()
        }
    }
}