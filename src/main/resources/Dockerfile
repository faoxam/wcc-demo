FROM mysql:latest

ENV MYSQL_ALLOW_EMPTY_PASSWORD=true
ENV MYSQL_DATABASE=wcc_demo
ENV MYSQL_ROOT_PASSWORD=password

COPY ./sql-scripts/ /docker-entrypoint-initdb.d/
COPY ./sql-scripts/ukpostcodes.csv /var/lib/mysql-files/
COPY ./sql-scripts/postcode-geo.sql .
