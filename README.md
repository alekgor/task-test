# task-test

## Docker
 - Docker-compose находится в /docker-compose, запустит инстанс PostgreSQL (порты 5433:5432), и само приложение (порты 8080:8080).
 - Запуск:
 ```sh
  docker-compose up 
```

## OpenApi
  - Добавил swagger openapi generator, страничка доступна по адресу http://localhost:8080/swagger-ui.html. Не совсем точная документация, к примеру тут описаны не все коды ответа http. Можно использовать как клиент.


## Spring-boot 
Приложение содержит два основных контроллера:
- ***EnergyLevelController*** отвечает за логику получения значения energyLevel
- ***QuoteController*** отвечает за логику добавления котировок

## Stack 
- ***spring-boot***
- ***spring-test***
- ***spring-jpa***
- ***spring-web***
- ***mockito***
- ***docker***
- ***maven***
- ***postgres***
