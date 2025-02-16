# Share It

## Описание проекта

Сервис для шеринга вещей. Сервис обеспечивает возможность пользователям рассказывать, какими вещами они готовы делиться,
находить нужную вещь, брать её в аренду, оставлять отзывы, создавать запросы на нужную вещь и добавлять её в ответ.
Приложение разработано на микросервисной архитектуре. Основной сервис содержит всю логику и основную функциональность, а
также взаимодействие с СУБД. Шлюз – сервис предназначенный для валидации входящих запросов от пользователей.

### Жизненный цикл

1. Владелец добавляет в приложение новую вещь. Вещь имеет статус - доступна ли она для аренды. Статус проставляет
   владелец.
2. Пользователь через поиск находит нужную вещь и подает запрос на ёё аренду.
3. Владелец вещи поддверждает аренду или нет.
4. После окончания срока аренды пользователь возвращает вещь владельцу.
5. Пользователь может оставить отзыв.

## Стэк

- Java 21
- Maven
- Spring Boot
- PostgreSQL
- H2
- JPA
- Hibernate
- Junit5
- Mockito
- Postman
- Docker
