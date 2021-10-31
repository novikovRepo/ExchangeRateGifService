Задание
---
```
Создать сервис, который обращается к сервису курсов валют, и отдает gif в ответ:  

если курс по отношению к рублю за сегодня стал выше вчерашнего,  
то отдаем рандомную отсюда https://giphy.com/search/rich  
если ниже - отсюда https://giphy.com/search/broke
  
Ссылки  

REST API курсов валют - https://docs.openexchangerates.org/  
REST API гифок - https://developers.giphy.com/docs/api#quick-start-guide
  
Must Have
  
Сервис на Spring Boot 2 + Java / Kotlin  
Запросы приходят на HTTP endpoint, туда передается код валюты  
Для взаимодействия с внешними сервисами используется Feign  
Все параметры (валюта по отношению к которой смотрится курс,   
адреса внешних сервисов и т.д.) вынесены в настройки  
На сервис написаны тесты   
(для мока внешних сервисов можно использовать @mockbean или WireMock)   
Для сборки должен использоваться Gradle  
Результатом выполнения должен быть репо на GitHub с инструкцией по запуску  
Nice to Have  
Сборка и запуск Docker контейнера с этим сервисом
```  
Создание образа и запуск контейнера
---
В проекте настроена автоматизация создания образа и создания контейнера с помощью плагинов
com.palantir.docker и com.palantir.docker-run, поэтому все что необходимо сделать
это стянуть проект и последовательно запустить следующие две команды находясь в корневой директории
проекта:
```
./gradlew docker
./gradlew dockerRun
```
Работа приложения
---
После запуска контейнера нужно воспользоваться следующей ссылкой:
```
http://localhost:8080/api/getgif/{currency code}
```
