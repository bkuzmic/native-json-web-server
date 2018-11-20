#!/usr/bin/env bash
mvn clean install

# build database
docker build -t bkuzmic/example-db:latest src/postgres-db/

# build native app
docker build \
   --build-arg GRAAL_ARGUMENTS="--no-server --verbose -cp target/*:target/lib/* \
      com.github.bkuzmic.web.Main \
      -H:EnableURLProtocols=http \
      -H:+ReportUnsupportedElementsAtRuntime \
      -H:+AllowVMInspection \
      -H:ReflectionConfigurationFiles=src/main/resources/reflection_config.json \
      --delay-class-initialization-to-runtime=io.undertow.server.protocol.ajp.AjpServerRequestConduit,io.undertow.server.protocol.ajp.AjpServerResponseConduit,org.xnio.channels.Channels \
      -H:Name=app" \
   -t bkuzmic/json-web-server:latest .