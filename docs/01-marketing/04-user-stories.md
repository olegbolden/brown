# Пользовательские истории

## 1. Создание уведомления

**В качестве** сотрудника отдела клиентского сервиса, \
**Я хочу** создать уведомление о перебоях с интернетом в сети оператора XXX, \
**Для того, чтобы** оповестить об этом сотрудников колл-центра.

1. Сценарий: переход к созданию уведомления. \
   **Дано:** я нахожусь на странице реестра уведомлений. \
   **Когда** я нажимаю кнопку "Добавить+", \
   **тогда** я попадаю на страницу создания нового уведомления, где необходимо выбрать его тип и видимость \
   **и** после выбора типа уведомления появляется остальной набор полей, связанных с этим типом.
2. Сценарий: заполнение набора полей, связанных с данным типом уведомления. \
   **Дано:** заполняю остальные поля уведомления. \
   **Когда** я нажимаю на кнопку "Сохранить" с незаполненными полями, \
   **тогда** я вижу предупреждение о необходимости заполнения этих полей. \
3. Сценарий: публикация уведомления. \
   **Дано:** я странице создания уведомления, \
   **И** все поля корректно заполнены.
   **Когда** я нажимаю кнопку "Сохранить", \
   **тогда** всплывает попап об успешном создании уведомления за номером N \
   **И** через 5 секунд происходит автоматический редирект в реестр уведомлений, в котором только что созданное уведомление будет находиться в конце списка.

## 2. Редактирование уведомления

**В качестве** сотрудника отдела клиентского сервиса, \
**Я хочу** внести коррективы в уже опубликованное уведомление о перебоях с интернетом в сети оператора XXX, \
**Для того, чтобы** оповестить об этом сотрудников колл-центра.

1. Сценарий: переход к просмотру уведомления. \
   **Дано:** я нахожусь на странице реестра уведомлений. \
   **Когда** я подвожу курсор мыши к номеру N нужного уведомления, \
   **тогда** он сменяется на указательный палец, по которому можно кликнуть мышью, \
   **и** попасть на страницу просмотра всех данных об уведомлении.
2. Сценарий: переход к редактированию уведомления. \
   **Дано:** я нахожусь на странице просмотра данных об уведомлении N. \
   **Когда** я нажимаю на кнопку "Редактировать", \
   **тогда** перехожу в окно редактирования уведомления. \
3. Сценарий: редактирование уведомления. \
   **Дано:** я странице редактирования уведомления, \
   **Когда** я вношу коррективы в редактируемые поля уведомления и нажимаю кнопку "Сохранить", \
   **тогда** всплывает попап об успешном редактировании уведомления \
   **И** происходит автоматический редирект на страницу просмотра данных об уведомлении N.

## 3. Деактивация уведомления

**В качестве** сотрудника отдела клиентского сервиса, \
**Я хочу** закрыть опубликованное уведомление о перебоях с интернетом в сети оператора XXX, \
**Для того, чтобы** оповестить сотрудников колл-центра о том, что инцидент исчерпан.

1. Сценарий: переход к просмотру уведомления. \
   **Дано:** я нахожусь на странице реестра уведомлений. \
   **Когда** я подвожу курсор мыши к номеру N нужного уведомления, \
   **тогда** он сменяется на указательный палец, по которому можно кликнуть мышью, \
   **и** попасть на страницу просмотра всех данных об уведомлении.
2. Сценарий: закрытие уведомления. \
   **Дано:** я нахожусь на странице просмотра данных об уведомлении N. \
   **Когда** я нажимаю на кнопку "Закрыть уведомление", \
   **тогда** всплывает попап об успешной деактивации уведомления \
   **И** происходит автоматический редирект в реестр уведомлений