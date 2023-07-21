### Stub Repository tests

When testing repos, work mode have to be set to 'test':
```
val ctx = NotificationContext(
    ...
    workMode = NotificationWorkMode.TEST,
    ...
)
```
Be careful to not confuse it with `NotificationWorkMode.STUB` mode since it is intended for working with stubs directly without involving any repository. Here we do the opposite - stubs are processed behind repository and therefore it is named `Stub Repository`.

Key point here is to set repository for `processor` to `NotificationStubRepo` like this: 
```kotlin   
private val processor by lazy { 
    NotificationProcessor(CorSettings(repoTest = NotificationStubRepo())) 
}
```
