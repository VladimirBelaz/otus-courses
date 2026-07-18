# Домашнее задание №3: Stub и API Testing

## Описание проекта
Реализован stub-сервер на WireMock для трёх эндпоинтов:
- `GET /user/get/{id}` – получение оценки пользователя;
- `GET /course/get/all` – список курсов;
- `GET /user/get/all` – список всех пользователей.

Все ответы соответствуют заданным JSON-контрактам. Написаны тесты на JUnit и Cucumber с использованием Dependency Injection (Guice).

## Технологии
- Java 17, Maven
- WireMock, JUnit 5, RestAssured
- Guice (DI), Jackson
- Cucumber (BDD)

## Запуск тестов
```bash
mvn clean test
```

## Структура
- `src/test/java/ru/otus/config` – Guice-модуль
- `src/test/java/ru/otus/helpers` – HTTP/SOAP-хелперы
- `src/test/java/ru/otus/stub` – WireMock-заглушки
- `src/test/java/ru/otus/tests` – JUnit-тесты с DI
- `src/test/java/ru/otus/steps` – шаги Cucumber
- `src/test/resources/schemas` – JSON-схемы
- `src/test/resources/features` – .feature-файлы

## Контакты
Автор: Владимир Белаз  
Репозиторий: [https://github.com/VladimirBelaz/otus-courses/tree/homework_3](https://github.com/VladimirBelaz/otus-courses/tree/homework_3)