### Stub work mode tests

When testing in `stub` work mode, responses are predefined by fixed stubs and depend upon request type only, not from request content. Hence, you can omit request object at all. 

'Stub' work mode is to be set within `NotificationContext`:
```
val ctx = NotificationContext(
    ...
    workMode = NotificationWorkMode.STUB,
    ...
)
```
Also, repository settings for `processor` have to be set to `NONE` by specifying just `CorSettings()`:
```
private val processor = NotificationProcessor(CorSettings())
```
