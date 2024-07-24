#  Шардированный кластер MongoDB с репликацией и кэшированием

Этот репозиторий содержит конфигурацию Docker Compose для настройки шардированного кластера MongoDB с двумя шардами,</br> 
каждый из которых имеет три реплики, а также сервер конфигурации.</br> 
А также Redis для кэширования.</br> 
Включает сервис `pymongo_api` для взаимодействия с кластером </br>
и настроенным на использование кэшированных данных в методе http://localhost:8000/helloDoc/users .



## Предварительные требования

- Docker
- Docker Compose

## Обзор сервисов

- **configSrv**: Сервер конфигурации MongoDB
- **shard1_primary**: Основная реплика первого шарда
- **shard1_secondary1**: Первая дополнительная реплика первого шарда
- **shard1_secondary2**: Вторая дополнительная реплика первого шарда
- **shard2_primary**: Основная реплика второго шарда
- **shard2_secondary1**: Первая дополнительная реплика второго шарда
- **shard2_secondary2**: Вторая дополнительная реплика второго шарда
- **mongos_router**: Роутер MongoDB (mongos)
- **redis**: используется для кэширования
- **pymongo_api**: Сервис API, использующий pymongo для взаимодействия с кластером MongoDB


## Конфигурация

### Конфигурация Docker Compose

Файл `docker-compose.yml` настраивает следующие сервисы:

- [docker-compose.yml](compose.yaml)

### Настройка кластера

1. **Запустите окружение Docker Compose**:

    ```bash
    export COMPOSE_PROJECT_NAME=sharding-repl-cachel
    docker-compose up -d
    ```

2. **Инициализируйте репликасет сервера конфигурации**:

   [init-configsrv.sh](scripts/init-configsvr.sh)

3. **Инициализируйте репликасет первого и второго шарда**

   [init-shard1.sh](scripts/init-shard1.sh)</br>
   [init-shard2.sh](scripts/init-shard2.sh)

4. **Настройте роутер (mongos):**

   [mongos_router](scripts/add-shards.sh)

5. **Выполните шардирование бд и коллекции:**

    [шардирование](scripts/mongo-init.sh)

6.  **Выполните проверку статуса шардирования:**
      ```bash
          docker exec -it mongos_router mongosh --port 27020 --eval 'sh.status()'
    ```
    в ответ должны получить 
```
shardingVersion
{ _id: 1, clusterId: ObjectId('669b9e8b5a31d82648444c6f') }
---
shards
[
  {
    _id: 'shard1',
    host: 'shard1/shard1_primary:27018,shard1_secondary1:27018,shard1_secondary2:27018',
    state: 1,
    topologyTime: Timestamp({ t: 1721474767, i: 2 })
  },
  {
    _id: 'shard2',
    host: 'shard2/shard2_primary:27019,shard2_secondary1:27019,shard2_secondary2:27019',
    state: 1,
    topologyTime: Timestamp({ t: 1721474767, i: 6 })
  }
]
---
....
```
## Проверка наличия записей в реплике
```shell
docker exec -it shard1_secondary2 mongosh --port 27018 --eval 
```
###  находясь внутри выполните по очереди</br>
**use somedb**</br>
**db.getMongo().setReadPref("secondary")**</br>
**db.helloDoc.countDocuments()**</br>

в результате получите 
```
shard1 [direct: secondary] somedb> db.helloDoc.countDocuments();
492

```

## Как проверить

### Если вы запускаете проект на локальной машине

Откройте в браузере http://localhost:8080

### Если вы запускаете проект на предоставленной виртуальной машине

Узнать белый ip виртуальной машины

```shell
curl --silent http://ifconfig.me
```

### Откройте в браузере http://<ip виртуальной машины>:8080

### В приложении кэширование доступно для эндпоинта /<collection_name>/users.</br>
### Проверьте скорость выполнения повторных запросов — она должна увеличиться.
## Чтобы убедиться, что ваше приложение правильно использует Redis для кэширования 
### выполните 
```shell
docker exec -it redis redis-cli
```
Находясь внутри выполните 
```
KEYS *
```

В ответ получите 
1) "api:cache::413c4c5b060defe0239317c05edff81a"
```
GET api:cache::413c4c5b060defe0239317c05edff81a
```
В ответ получите 
> "{\"users\": [{\"_id\": \"669cc637a213a8234c149f48\", \"age\": 0, \"name\": \"ly0\"}...
