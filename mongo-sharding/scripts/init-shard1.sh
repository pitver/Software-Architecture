docker exec -it shard1 mongosh --port 27018 --eval '
rs.initiate(
  {
    _id : "shard1",
    members: [
      { _id : 0, host : "shard1:27018" }
    ]
  }
)'


