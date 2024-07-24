# Шардированный кластер MongoDB с использованием Docker Compose

Этот репозиторий содержит конфигурацию Docker Compose для настройки шардированного кластера MongoDB с двумя шардами и сервером конфигурации.</br> 
Также он включает сервис `pymongo_api` для взаимодействия с кластером.

## Предварительные требования

- Docker
- Docker Compose

## Обзор сервисов

- **configSrv**: Сервер конфигурации MongoDB
- **shard1**: Первый шард
- **shard2**: Второй шард
- **mongos_router**: Роутер MongoDB (mongos)
- **pymongo_api**: Сервис API, использующий pymongo для взаимодействия с кластером MongoDB

## Конфигурация

### Конфигурация Docker Compose

Файл `docker-compose.yml` настраивает следующие сервисы:

- [docker-compose.yml](compose.yaml)

### Настройка кластера

1. **Запустите окружение Docker Compose**:

    ```bash
    export COMPOSE_PROJECT_NAME=mongo-sharding
    docker-compose up -dd
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
   { _id: 1, clusterId: ObjectId('669b745a53d06ce60c58b82e') }
---
shards
[
{
_id: 'shard1',
host: 'shard1/shard1:27018',
state: 1,
topologyTime: Timestamp({ t: 1721463948, i: 2 })
},
{
_id: 'shard2',
host: 'shard2/shard2:27019',
state: 1,
topologyTime: Timestamp({ t: 1721463948, i: 6 })
}
]
....
```

## Как проверить

### Если вы запускаете проект на локальной машине

Откройте в браузере http://localhost:8080

### Если вы запускаете проект на предоставленной виртуальной машине

Узнать белый ip виртуальной машины

```shell
curl --silent http://ifconfig.me
```

Откройте в браузере http://<ip виртуальной машины>:8080

### выполните запрос GET/Root 

В ответ увидите список доступных шардов