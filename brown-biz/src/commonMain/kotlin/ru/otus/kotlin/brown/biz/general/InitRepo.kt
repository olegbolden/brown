package ru.otus.kotlin.brown.biz.general

import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.common.helpers.fail
import ru.otus.kotlin.brown.common.NotificationContext
import ru.otus.kotlin.brown.common.helpers.errorAdministration
import ru.otus.kotlin.brown.common.repo.INotificationRepository
import ru.otus.kotlin.brown.common.models.NotificationWorkMode

fun ICorChainDsl<NotificationContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Tries to set up main repository depending on chosen work mode
    """.trimIndent()
    handle {
        notificationRepo = when {
            workMode == NotificationWorkMode.TEST -> settings.repoTest
            workMode == NotificationWorkMode.STUB -> INotificationRepository.NONE
            else -> settings.repoProd
        }

        if (workMode != NotificationWorkMode.STUB && notificationRepo == INotificationRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is not configured for $workMode work mode. " +
                        "Contact the administrator staff, please."
            )
        )
    }
}
