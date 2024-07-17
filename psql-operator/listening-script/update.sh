#!/bin/bash

docker build -t tonyxie2017/pgsql-listener:latest .
docker push tonyxie2017/pgsql-listener:latest
kubectl set image statefulset/verify-db pgsql-listener=tonyxie2017/pgsql-listener:latest
