#!/bin/bash
for i in $(seq 1 $1)
do
  uuid="$(uuidgen)";
  curl -H "Content-Type: application/json" -d "{ \"playerId\": \"$(echo $uuid)\"}" http://localhost:8080/api/queue;
done
