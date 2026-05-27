# OTUS Catalog Tests

Автотесты для каталога курсов [OTUS](https://otus.ru/catalog/courses)

## Требования

- Java 26
- Maven 3.8+
- Chrome браузер

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/VladimirBelaz/otus-courses.git
```

### 2. Запуск тестов

```bash
mvn clean test
```

### 3. Запуск с указанием браузера

```bash
mvn clean test -Dbrowser=chrome
```

## Структура проекта

```text
src/main/java/
├── annotations/     # Аннотации для маршрутизации
├── commons/         # Базовые классы
├── components/      # Переиспользуемые компоненты (Header, CookiePopup)
├── elements/        # Кастомные WebElement (Button, Link, TextBlock)
├── exceptions/      # Пользовательские исключения
├── factory/         # Фабрика драйверов с листенерами
├── listeners/       # Слушатели для подсветки элементов
├── modules/         # DI модули (Google Guice)
├── pages/           # Page Objects
└── utils/           # Утилиты (Waiters, DateUtils)

src/test/java/
├── assertions/      # Кастомные ассерты
├── extensions/      # Расширения JUnit
├── helpers/         # Вспомогательные классы для тестов
└── CatalogTests.java
```

## Реализованные сценарии

### Сценарий 1

- Открыть страницу каталога курсов
- Найти курс по имени (через stream API)
- Кликнуть по плитке курса
- Проверить, что открыта страница верного курса

### Сценарий 2

- Найти курсы, которые стартуют раньше и позже всех (через reduce)
- Проверить данные на карточке курса через Jsoup

### Сценарий 3

- Открыть главную страницу
- В меню «Обучение» выбрать случайную категорию
- Проверить, что открыт каталог курсов верной категории

## Технологии

- Java 26
- Selenium WebDriver 4.43.0
- JUnit 5
- Google Guice (DI)
- WebDriverManager
- Jsoup
- Maven

## Линтеры

- SpotBugs
- Checkstyle
