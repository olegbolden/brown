# API
## Сущности

1. Notification - (уведомление)
2. Notification Event (событие, связанное с уведомлением)

## Описание сущности Notification

1. Info
   1. Title (6-128 символов)
   2. Description (100-10000 символов)
   3. OwnerId (UserId)
   4. Visibility (приватное, общедоступное)
2. Type (common, warning, alert) - тип уведомления - "обычное", "предупреждение", "внимание!"
3. Status (open, closed) - статус активности - "открыто", "закрыто"

## Описание сущности Notification Event

1. ID - ID уведомления, с которым связано событие
2. Type (common, warning, alert) - тип уведомления - "обычное", "предупреждение", "внимание!"
3. Status (open, closed, updated) - характер события, описывающего то, что произошло с уведомлением - "открыто", "закрыто", "изменено"
4. CreatedAt - момент времени, в который произошло событие

## Функции (эндпониты)

1. Notification CRUDS (Rest)
   1. create
   2. read
   3. update
   4. cancel
   5. search - поиск по фильтрам
2. Notification Event (WebSocket)
   1. event 
