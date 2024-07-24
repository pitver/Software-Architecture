docker exec -it shard2 mongosh --port 27019 --eval '
rs.initiate(
  {
    _id : "shard2",
    members: [
      { _id : 0, host : "shard2:27019" }
    ]
  }
)'
