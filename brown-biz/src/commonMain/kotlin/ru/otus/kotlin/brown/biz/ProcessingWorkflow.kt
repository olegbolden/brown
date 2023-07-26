package ru.otus.kotlin.brown.biz

import ru.otus.kotlin.brown.biz.general.initRepo
import ru.otus.kotlin.brown.biz.general.prepareResult
import ru.otus.kotlin.brown.cor.*
import ru.otus.kotlin.brown.biz.groups.*
import ru.otus.kotlin.brown.biz.repo.*
import ru.otus.kotlin.brown.biz.workers.*
import ru.otus.kotlin.brown.biz.validation.*
import ru.otus.kotlin.brown.common.models.*
import ru.otus.kotlin.brown.common.NotificationContext

interface ProcessingWorkflow {
    companion object {
        val stubBusinessChain = rootChain<NotificationContext> {
            initStatus("Status initialization")
            initRepo("Repository setup")
            operation("Create notification scenario", NotificationCommand.CREATE) {
                stubs("Stub processing") {
                    stubCreateSuccess("Notification successfully created")
                    stubValidationBadTitle("Invalid title")
                    stubValidationBadDescription("Invalid description")
                    stubDbError("DB error occurred")
                    stubNoCase("Requested stub does not exist")
                }
                validation {
                    worker("Copying fields to requestNotificationValidating") { requestNotificationValidating = requestNotification.deepCopy() }
                    worker("Cleaning of id") { requestNotificationValidating.id = NotificationId.NONE }
                    worker("Cleaning of title") { requestNotificationValidating.title = requestNotificationValidating.title.trim() }
                    worker("Cleaning of description") { requestNotificationValidating.description = requestNotificationValidating.description.trim() }
                    validateTitleNotEmpty("Check if the title is not empty")
                    validateTitleHasContent("Check content validity")
                    validateDescriptionNotEmpty("Check if the description is not empty")
                    validateDescriptionHasContent("Check content validity")

                    finishNotificationValidation("Validation successfully finished")
                }
                chain {
                    title = "Saving logic"
                    repoPrepareCreate("Prepare saving notification to DB")
                    repoCreate("Saving notification to DB")
                }
                prepareResult("Building response")
            }
            operation("Read notification scenario", NotificationCommand.READ) {
                stubs("Stub processing") {
                    stubReadSuccess("Notification successfully read")
                    stubValidationBadId("Validation of Id failed")
                    stubDbError("DB error occurred")
                    stubNoCase("Requested stub does not exist")
                }
                validation {
                    worker("Copying fields to requestNotificationValidating") { requestNotificationValidating = requestNotification.deepCopy() }
                    worker("Cleaning of id") { requestNotificationValidating.id = NotificationId(requestNotificationValidating.id.asString().trim()) }
                    validateIdNotEmpty("Check if the Id is not empty")
                    validateIdProperFormat("Check of id format validity")

                    finishNotificationValidation("Validation successfully finished")
                }
                chain {
                    title = "Reading logic"
                    repoRead("Read notification from DB")
                    worker {
                        title = "Prepare response for Read request"
                        on { state == NotificationState.RUNNING }
                        handle { notificationRepoDone = notificationRepoRead }
                    }
                }
                prepareResult("Prepare response")
            }
            operation("Update notification scenario", NotificationCommand.UPDATE) {
                stubs("Stub processing") {
                    stubUpdateSuccess("Notification successfully updated")
                    stubValidationBadId("Validation of Id failed")
                    stubValidationBadTitle("Validation of title failed")
                    stubValidationBadDescription("Validation of description failed")
                    stubDbError("DB error occurred")
                    stubNoCase("Requested stub does not exist")
                }
                validation {
                    worker("Copying fields to requestNotificationValidating") { requestNotificationValidating = requestNotification.deepCopy() }
                    worker("Id clean up") { requestNotificationValidating.id = NotificationId(requestNotificationValidating.id.asString().trim()) }
                    worker("Lock clean up") { requestNotificationValidating.lock = NotificationLock(requestNotificationValidating.lock.asString().trim()) }
                    worker("Cleaning of title") { requestNotificationValidating.title = requestNotificationValidating.title.trim() }
                    worker("Cleaning of description") { requestNotificationValidating.description = requestNotificationValidating.description.trim() }
                    validateIdNotEmpty("Check if the Id is not empty")
                    validateIdProperFormat("Check of Id format validity")
                    validateLockNotEmpty("Check if the lock is not empty")
                    validateLockProperFormat("Check of lock format validity")
                    validateTitleNotEmpty("Check if the title is not empty")
                    validateTitleHasContent("Check content validity")
                    validateDescriptionNotEmpty("Check if the description is not empty")
                    validateDescriptionHasContent("Check content validity")

                    finishNotificationValidation("Validation successfully finished")
                }
                chain {
                    title = "Saving logic"
                    repoRead("Reading notification from DB")
                    repoPrepareUpdate("Prepare notification for update")
                    repoUpdate("Update procedure")
                }
                prepareResult("Prepare response")
            }
            operation("Cancel notification scenario", NotificationCommand.CANCEL) {
                stubs("Stub processing") {
                    stubCancelSuccess("Notification successfully cancelled")
                    stubValidationBadId("Validation of Id failed")
                    stubDbError("DB error occurred")
                    stubNoCase("Requested stub does not exist")
                }
                validation {
                    worker("Copying fields to requestNotificationValidating") {
                        requestNotificationValidating = requestNotification.deepCopy()
                    }
                    worker("Id clean up") { requestNotificationValidating.id = NotificationId(requestNotificationValidating.id.asString().trim()) }
                    worker("Lock clean up") { requestNotificationValidating.lock = NotificationLock(requestNotificationValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Check if the Id is not empty")
                    validateIdProperFormat("Check of id format validity")
                    validateLockNotEmpty("Check if the lock is not empty")
                    validateLockProperFormat("Check of lock format validity")
                    finishNotificationValidation("Validation successfully finished")
                }
                chain {
                    title = "Cancellation logic"
                    repoRead("Retrieving notification from DB")
                    repoPrepareCancel("Prepare notification for cancellation")
                    repoCancel("Cancellation of notification")
                }
                prepareResult("Prepare response")
            }
            operation("Search notification scenario", NotificationCommand.SEARCH) {
                stubs("Stub processing") {
                    stubSearchSuccess("Notification successfully cancelled")
                    stubValidationBadId("Validation of Id failed")
                    stubDbError("DB error occurred")
                    stubNoCase("Requested stub does not exist")
                }
                validation {
                    worker("Copying fields to requestNotificationFilterValidating") { requestNotificationFilterValidating = requestNotificationFilter.copy() }

                    finishNotificationFilterValidation("Validation successfully finished")
                }
                repoSearch("Search notifications in DB by filter")
                prepareResult("Prepare response")
            }
        }.build()
    }
}

