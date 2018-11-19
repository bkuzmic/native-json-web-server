#!/bin/sh
export PG_SERVER_IP=$(getent hosts ${PG_SERVER_HOST} | awk '{ print $1 }')
export PG_DATABASE_URL=jdbc:postgresql://${PG_SERVER_IP}:5432/${PG_DATABASE_NAME}
./app