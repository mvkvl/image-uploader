# Задача

Реализовать простое REST API с одним единственным методом, который загружает изображения.

## Требования:
- Возможность загружать несколько файлов.
- Возможность принимать multipart/form-data запросы.
- Возможность принимать JSON запросы с BASE64 закодированными изображениями.
- Возможность загружать изображения по заданному URL (изображение размещено где-то в интернете).
- Создание квадратного превью изображения размером 100px на 100px.

## Опционально:
- Корректное завершение приложения при получении сигнала ОС (graceful shutdown).
- Dockerfile и docker-compose.yml, которые позволяют поднять приложение единой docker-compose up командой.
- Модульные тесты, функциональные тесты, CI интеграция (Travis CI, Circle CI, другие).

# Решение

## Архитектура приложения:

Приложение реализовано на Java с использованием библитеки Spring Boot.

![software diagram](https://github.com/mvkvl/image-uploader/raw/master/docs/SoftwareDiagram.png "software diagram")

Приложение представляет собой набор нескольких модулей (микросервисов):
* uploader - собственно, основной сервис, реализующий загрузку изображений
* converter - генератор уменьшенных копий загруженных изображений
* viewer - модуль для доступа к загруженным изображениям

Загрузив изображение, uploader отправляет сообщение об этом в очередь сообщений. Converter принимает это сообщение и создаёт уменьшенную копию загруженного изображения.

Сейчас вся обработка запросов на загрузку реализована прямо в сервисе upload. Можно пойти дальше и, допустим, загрузку изображений из интернета по предоставленным ссылкам производить в отдельном сервисе (downloader), которому uploader будет отправлять ссылку через очередь сообщений.

## Конфигурация
В общем случае можно реализовать конфигурацию в виде stream-cloud-config и управлять вопросами конфигурации сервисов централизованно. На текущий момент конфигурация реализована в виде отдельных файлов application.yml для каждого из трёх сервисов (см. примеры в директории /docker).


### DataStore
Загруженные изображения сохраняются в DataStore. Сейчас есть две реализации: FileSystemDataStore и MongoDBDataStore. При необходимости можно дописывать новые. Какая из них используется, определяется параметром в конфигурационном файле (application.yml):

```
images:
  datastore:
    type: fs # supported values: fs, mongodb
```

#### FileSystemDataStore
При использовании FileSystemDataStore необходимо определить пути, куда будут сохраняться загруженные файлы и их копии:

```
images:
  datastore:
    type: fs
    fs:
      path:
        upload:  "/path/to/upload/images"
        preview: "/path/to/preview/images"
```

> Доступ к этим директориям нужен всем трём сервисам, поэтому либо они должны находиться на общедоступных сетевых дисках, либо (как в тестовом случае) Docker-контейнеры, в которых запускаются сервисы, должны монтировать эти директории с host-системы.

#### MongoDBDataStore

При использовании MongoDBDataStore необходимо указать параметры подключения к MongoDB в настройках сервисов:

```
spring:
  data:
    mongodb:
      host: <host>
      port: <port>
images:
  datastore:
    type: mongodb
```

### RabbitMQ

Также необходимо настроить параметры подключения к RabbitMQ для сервисов uploader и converter:
```
spring:
  rabbitmq:
    host: <host>
    port: <port>
    username: guest
    password: guest
```

Для сервиса converter также должна быть указана следующая конфигурация spring.cloud.stream:

```
spring:
  cloud:
    stream:
      bindings:
        input:
          destination: images.preview.queue
```

### Viewer Service
Для создания ссылок на загруженные
изображения в настройках сервиса viewer надо указать URL-пути, по которым доступны изображения:

```
images:
  view:
    path:
      image:   "image/view"
      preview: "preview/view"
```

> обсуждабельно; может быть, можно было и по-другому это реализовать

## Использование
В корневой директории есть скрипты для сборки, запуска и остановки сервисов.

- Сборка всего проекта осуществляется командой
```
./build
```

- Запуск сервисов в Docker'e
```
./run -t {fs | mongo} [-l]
```

- Запуск сервисов с FileSystemDatastore
```
./run -t fs
```

- Запуск сервисов с MongoDBDatastore
```
./run -t mongo
```

> Для вывода логов сервисов в консоль при запуске используйте ключ '-l'

- Остановка сервисов
```
./stop -t {fs | mongo}
```

- Остановка и очистка
```
./stop_and_clean
```

> одновременно запустить обе реализации (и с FileSystemDatastore, и с MongoDBDatastore) не получится, т.к. сервисы bind'ятся на одни и те же порты (для работы тестовых клиентов).

## Использование / тестирование
В дирекории ./code/test-client лежат тестовые клиенты (консольные и html/js). Консольные написаны на bash-script. Для их работы нужны установленные curl, base64 и jq.

### Клиент HTML/JS:
Для его использования нужно просто открыть файл js_client.html в браузере.

### Консольные клиенты:
```
./test_base64.sh /img/image1.png /img/image2.jpeg


./test_multipart.sh /img/image1.png /img/image2.jpeg


# ссылки на загрузку прописаны в самом скрипте
./test_url.sh

# нагрузочный тест (перед запуском надо поправить в нём пути до файлов для загрузки)
./load_test.sh
```

# API

## Uploader

По-умолчанию сервис доступен на порту 7070.

#### <<host:port>>/upload - загрузка изображений

- ##### application/x-www-form-urlencoded
@RequestParam url - список URL для загрузки из интернета

- ##### multipart/form-data
@RequestParam file - список файлов для загрузки

- ##### application/json
@RequestBody - JSON-массив типа Base64EncodedImageJson:
```
{
      "name"   : "...",
      "type"   : "...",
      "base64" : "..."
}
```
- name - имя файла (необязательное поле)
- type - MIME-тип файла
- base64 - Base64-закодированное изображение

Строка Base64 может начинаться с "data:image/<type>;base64,iVBORw0K....". Если это так, MIME-тип будет взят отсюда, и тогда поле type не обязательно. Если строка Base64 содержит только данные изображения (например, в результате выполнения команды base64 для кодирования изображения), то необходимо корректно указывать MIME.

## Viewer
По-умолчанию сервис доступен на порту 7071.

##### <<host:port>>/image - вывод списком информации обо всех загруженных изображениях
```
[{
  "id"   : "...",
  "name" : "...",
  "link" : "..."
}]
```
##### <<host:port>>/image/{id} - вывод информации об изображении
```
{
  "id"   : "...",
  "name" : "...",
  "link" : "..."
}
```
##### <<host:port>>/image/view/{id} - вывод изображения

##### <<host:port>>/preview - вывод списком информации обо всех уменьшенных копиях изображений
```
[{
  "id"   : "...",
  "name" : "...",
  "link" : "...",
  "original-link": "..."
}]
```
##### <<host:port>>/preview/{id} - вывод информации об уменьшенной копии изображения
```
{
  "id"   : "...",
  "name" : "...",
  "link" : "...",
  "original-link": "..."
}
```
##### <<host:port>>/preview/view/{id} - вывод уменьшенной копии изображения
