# Документация по развёртыванию системы в Docker Compose

Эта документация описывает шаги для развёртывания системы с использованием Docker Compose. Система включает следующие компоненты:

- Zookeeper
- Kafka
- PostgreSQL
- pgAdmin
- Device Management Service
- Telemetry Management Service
- Kong API Gateway

## Шаг 1: Установка Docker и Docker Compose

Перед началом убедитесь, что на вашей машине установлены Docker и Docker Compose. Если они не установлены, следуйте инструкциям ниже:

- **Установка Docker:**
    - [Инструкция по установке Docker](https://docs.docker.com/get-docker/)

- **Установка Docker Compose:**
    - [Инструкция по установке Docker Compose](https://docs.docker.com/compose/install/)
  
## Шаг 2: Подготовка файлов конфигурации
    
### Создайте файл [db-init/init.sql](db-init/init.sql) - для инициализации базы данных PostgreSQL
### Создайте файл [kong/kong.yml](kong/kong.yml) - конфигурация для Kong API Gateway

## Шаг 3: Создание `docker-compose.yml`

Создайте файл `dockerfile` в корне вашего каждого микросериса(Device Management Service/Telemetry Management Service). Этот файл будет содержать команды для сборки образа. </br>
Создайте файл `docker-compose.yml` в корне вашего проекта. Этот файл будет содержать описание всех сервисов, которые вы хотите развернуть.

### Пример [docker-compose.yml](docker-compose.yml)

## Шаг 4: Запуск контейнеров 
```
docker-compose up -d
```
## Шаг 5: Тестирование сервисов
### Так как у нас включен плагин JWT то необходимо создать токен - выполните скрипт [kong/tokenGen.py](kong/tokenGen.py) без него - классическая 401


![img_7.png](static/image/img_7.png)

```
получение роутов - curl --location 'http://localhost:8001/routes'
```
![img_1.png](static/image/img_1.png)

```
обновление конфигов - curl --location 'http://localhost:8001/config' \
--header 'Content-Type: text/yaml' \
--data '@/D:/Project/architecture-sprint-3/Task2/kong/kong.yml'
```
![img_2.png](static/image/img_2.png)

```
Создание устройства с учетом полученного токена -
curl --location 'http://localhost:8000/device/api/devices' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ1c2VyIn0.nCfxOkKF--DKrV5zkTERZtrWGchyJlXP8pdbzGs5h7w' \
--data '{
"id": 0,
"deviceId": "stу",
"status": "stу",
"lastUpdate": "2024-08-11T08:42:03.232Z"
}'
```
![img_3.png](static/image/img_3.png)

в логах сервиса девайсов увидим
![img_5.png](static/image/img_5.png)

в логах телеметрии увидим 
![img_6.png](static/image/img_6.png)

### Данный факт показывает что API по добавлению нового устройства в DeviceManagement работает, </br>а также настроен асинхронный обмен между DeviceManagement и TelemetryManagement