docker exec -it mongos_router mongosh --port 27020 --eval '
sh.addShard("shard1/shard1_primary:27018,shard1_secondary1:27018,shard1_secondary2:27018");
sh.addShard("shard2/shard2_primary:27019,shard2_secondary1:27019,shard2_secondary2:27019");
'

