package ru.otus.kotlin.brown.app.ktor.settings

import io.ktor.server.application.*
import ru.otus.kotlin.brown.common.CorSettings
import ru.otus.kotlin.brown.biz.NotificationProcessor
import ru.otus.kotlin.brown.log.common.LoggerProvider
import ru.otus.kotlin.brown.repo.postgresql.PostgresqlRepo
import ru.otus.kotlin.brown.repo.inmemory.NotificationInMemoryRepo

fun Application.initAppSettings(): AppSettings {

    val ktorConfig = environment.config
    val dataSourceConfig = DataSourceConfig.createDataSourceConfig(ktorConfig)
    val dataSource = DataSourceConfig.createDataSource(dataSourceConfig)
    DataSourceConfig.flywayMigrate(dataSource, dataSourceConfig)

    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = NotificationInMemoryRepo(),
        repoProd = PostgresqlRepo(dataSource)
    )
    return AppSettings(
        appUrls = ktorConfig.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = NotificationProcessor(corSettings),
    )
}

expect fun Application.getLoggerProviderConf(): LoggerProvider
