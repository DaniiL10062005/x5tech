# Bank Card Form

Тестовое задание на Kotlin Multiplatform: экран формы банковской карты с валидацией, определением типа карты,
маскированием данных и unit-тестами.

## Описание задания

Необходимо реализовать экран ввода банковской карты с использованием Kotlin Multiplatform и архитектурного
подхода MVVM.

Форма поддерживает:
- ввод номера карты;
- ввод имени держателя;
- ввод срока действия;
- ввод CVV;
- валидацию всех полей;
- определение типа карты;
- маскирование чувствительных данных;
- сохранение карты через репозиторий;
- unit tests.

## Реализованный функционал

Реализовано:
- форма ввода банковской карты;
- `MVVM` с `StateFlow`;
- визуальное отображение карты с данными;
- валидация номера карты;
- алгоритм Луна для проверки номера;
- определение типа карты:
  - `VISA`
  - `MASTERCARD`
  - `MIR`
  - `UNKNOWN`
- валидация имени держателя:
  - только латиница и пробел;
  - uppercase;
  - минимум 2 символа;
- валидация срока действия:
  - формат `MM/YY`;
  - месяц `01..12`;
  - дата не должна быть в прошлом;
- валидация CVV:
  - ровно 3 цифры;
- форматирование номера карты;
- маскирование номера карты, имени и CVV;
- fake-репозиторий для сохранения без backend;
- unit-тесты для use cases и `ViewModel`;
- DI через Koin;
- Compose UI-компоненты:
  - экран формы;
  - переиспользуемое поле ввода;
  - визуальное отображение карты.

## Архитектурный подход

Проект разделен на два модуля:

- `model`
  - доменные сущности;
  - контракты;
  - repository;
- `feature`
  - `BankCardFormViewModel`;
  - `BankCardFormState`;
  - use cases сценария формы;
  - Compose UI;
  - DI-модуль;
  - тесты `ViewModel`.

Подход:
- UI не содержит бизнес-валидации;
- `ViewModel` управляет состоянием формы и координирует use cases;
- логика форматирования, маскирования и проверки вынесена в отдельные use cases;
- зависимости передаются через конструктор и Koin.

Поток данных:
- пользовательское действие из UI;
- вызов метода `ViewModel`;
- форматирование / валидация / определение типа карты;
- обновление `StateFlow`;
- перерисовка UI.

## Стек технологий

- Kotlin Multiplatform
- MVVM
- Compose / Compose Multiplatform
- Koin
- Kotlinx Coroutines
- StateFlow
- Unit tests
- Kotlin Test

### Использование Ktor Client и Kotlinx Serialization

В текущей версии проекта `Ktor Client` и `Kotlinx Serialization` не используются в runtime-логике.

Причина:
- для тестового задания выбран `FakeCardRepository`, чтобы сфокусироваться на архитектуре, валидации, UI,
  тестируемости и многоплатформенной бизнес-логике;
- в проекте отсутствует реальный backend-контур, поэтому полноценный network/data слой с `Ktor Client` и DTO через
  `Kotlinx Serialization` был бы формальным и не добавлял бы пользы текущему сценарию.

Почему это решение осознанное:
- основная ценность задания в данной реализации сосредоточена в `ViewModel`, use cases, маскировании,
  валидации и unit tests;
- репозиторий оставлен абстракцией, поэтому при необходимости `FakeCardRepository` можно заменить на
  `CardRepositoryImpl`, использующий `Ktor Client` и `Kotlinx Serialization`, без изменения UI-слоя и
  основной логики формы.

Если расширять проект дальше, логичное место для интеграции:
- `Ktor Client`:
  в реализации `CardRepository` для `POST/GET` запросов;
- `Kotlinx Serialization`:
  в DTO-моделях request/response и конфигурации `Json`.

## Структура проекта

```text
root
├─ app
├─ model
│  └─ src
│     ├─ commonMain
│     │  └─ kotlin/com/example/x5tech/model
│     └─ commonTest
├─ feature
│  └─ src
│     ├─ commonMain
│     │  └─ kotlin/com/example/x5tech/feature/cardform
│     └─ commonTest
├─ gradle
├─ settings.gradle.kts
└─ build.gradle.kts
```

Основные файлы:

### model
- `domain/BankCard.kt`
- `domain/CardType.kt`
- `domain/CardValidationResult.kt`
- `repository/CardRepository.kt`
- `repository/FakeCardRepository.kt`

### feature
- `BankCardFormState.kt`
- `BankCardFormViewModel.kt`
- `KmpViewModel.kt`
- `BankCardFormModule.kt`
- `BankCardFormScreen.kt`
- `components/CardVisual.kt`
- `components/CardInputField.kt`
- `domain/ValidateCardNumberUseCase.kt`
- `domain/ValidateExpiryDateUseCase.kt`
- `domain/ValidateCardHolderNameUseCase.kt`
- `domain/ValidateCvvUseCase.kt`
- `domain/DetectCardTypeUseCase.kt`
- `domain/FormatCardNumberUseCase.kt`
- `domain/MaskCardDataUseCase.kt`

## Инструкция по запуску

### Требования
- JDK 11+
- Android Studio
- Android SDK

### Шаги
1. Клонировать репозиторий.
2. Открыть проект в Android Studio.
3. Убедиться, что настроен `JAVA_HOME`.
4. Синхронизировать Gradle.
5. Запустить Android-конфигурацию из модуля `app`.

### Запуск тестов

```bash
./gradlew test
```

Для Windows:

```bash
gradlew.bat test
```

## Описание тестов

Покрыты:
- форматирование номера карты;
- валидация номера карты;
- проверка алгоритма Луна;
- определение типа карты;
- валидация срока действия;
- валидация имени держателя;
- валидация CVV;
- маскирование данных;
- поведение `BankCardFormViewModel`.

Примеры сценариев:
- кнопка Save активируется только при валидной форме;
- корректное определение `VISA` и `MASTERCARD`;
- обновление validation state при невалидном номере;
- переключение masked/unmasked режима;
- вызов репозитория при успешном сохранении.

## Скриншоты

### Form Screen
![Form Screen](./docs/screenshots/form-screen.png)

### Masked Mode
![Masked Mode](./docs/screenshots/masked-mode.png)

### Validation Errors
![Validation Errors](./docs/screenshots/validation-errors.png)

## Видео

Демо-видео:
[Ссылка на видео](PASTE_VIDEO_LINK_HERE)

## APK

Скачать APK:
[Ссылка на APK](PASTE_APK_LINK_HERE)

## Использованные ИИ-инструменты

При выполнении задания использовались ИИ-инструменты как вспомогательный инструмент проектирования и генерации
чернового кода:
- ChatGPT / Codex

ИИ использовался для:
- проектирования структуры модулей;
- генерации шаблонов use cases;
- генерации базовых unit-тестов;
- подготовки README.

Финальная архитектура, структура проекта, правки кода и адаптация решений выполнялись вручную с учетом требований
задания.

## Примечания

- В проекте используется `FakeCardRepository` вместо реального backend.
- Логика времени вынесена через `CurrentDateProvider`, чтобы валидация срока действия была тестируемой.
- Основной фокус решения: чистая структура, тестируемость, читаемость и изоляция бизнес-логики от UI.
