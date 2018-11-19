# Native Java Json Web Server using GraalVM native-image

This is example of running Undertow HTTP server with /user and /user/:id endpoints.
It runs in it's own Docker container and reads user details from PostgreSQL database running in separate Docker container.

How to setup and execute example:

NOTE: Make sure you have Linux, Docker, docker-compose and Maven with Java 1.8 installed.

Build the project using command:
> ./build.sh

This will build the maven project, prepare database Docker image and call GraalVM's native-image command to produce native binary.
Then, this native binary will be provided to create small Alpine-based Docker image.

Run using docker-compose:
> docker-compose up

Access running HTTP server endpoints to get Json response:
> curl http://127.0.0.1:4567/user/

or

> curl http://127.0.0.1:4567/user/1

### Stats
Application starts in 1-3ms (on Core i7) and Docker statistics show memory usage of only 13.5MB on startup.
Docker image size is 33.1MB.

### Additional notes
Fetching the data from database is provided using fluent API of [JOOQ](https://www.jooq.org/) and [Elegant Object's SQL-speaking objects](https://www.yegor256.com/2014/12/01/orm-offensive-anti-pattern.html).

Custom dummy classes are provided in graal-native source folder since GraalVM will not compile the native image without them. All of them are for PostgreSQL driver dependency. 

I had to implement really basic connection pooling from the scratch since none of the existing connection pooling libraries (HikariCP, C3P0, BoneCP or DBCP) work with native-image yet. 
  