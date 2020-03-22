## PostgreSQL with Docker
```sh
# install postgres container
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=postgres -d postgres:9.6

# connect postgres container using bash
docker exec -i -t postgres /bin/bash

# change to postgres user
su - postgres

# 
psql

# create database
create database demo;

# create user and grant
create user kdevkr with encrypted password 'password';
grant all privileges on database demo to kdevkr;
```