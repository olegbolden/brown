### In Memory Repository tests

When testing repos, work mode have to be set to 'test':
```
val ctx = NotificationContext(
    ...
    workMode = NotificationWorkMode.TEST,
    ...
)
```

Key point here is to set repository for `processor` to `NotificationInMemoryRepo` like this: 
```kotlin   
private val processor by lazy { 
    NotificationProcessor(CorSettings(repoTest = NotificationInMemoryRepo())) 
}
```
