 #Включите шардирование для базы данных:
docker exec -it mongos_router mongosh --port 27020 --eval 'sh.enableSharding("somedb")'
 #Настройте шардирование для коллекции:
docker exec -it mongos_router mongosh --port 27020 --eval 'sh.shardCollection("somedb.helloDoc", { "name": "hashed" })'
 #Вставьте документы в коллекцию:
docker exec -it mongos_router mongosh --port 27020 --eval
#находясь внутри выполните по очереди
> use somedb;
> for (var i = 0; i < 1000; i++) db.helloDoc.insert({ age: i, name: "ly" + i });
> db.helloDoc.countDocuments();

docker exec -it mongos_router mongosh --port 27020 --eval 'sh.status()'


