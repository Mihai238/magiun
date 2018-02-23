#!/usr/bin/env bash

docker run -d -p 27017:27017 --mount source=mongo-volume,target=/data/db --name mongo mongo

if [ "$1" == "--importData" ]; then
    echo "Waiting for mongo to get up"
    sleep 10s

    docker cp drinks.csv mongo:/tmp
    docker exec -i mongo bash <<'EOF'
    mongoimport --db testDb --collection testCollection --type csv --headerline /tmp/drinks.csv
    exit
EOF
fi