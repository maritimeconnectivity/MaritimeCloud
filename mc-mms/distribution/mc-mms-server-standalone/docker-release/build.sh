#!/bin/bash

if [ "$1" = "build" ]; then
  if [ -z "$2" ]; then
    echo "No version supplied"
    exit
  fi

  ./mavenget.sh mc-mms-server-standalone $2
  cp ../docker/start.sh .
  docker build -t "dmadk/mc-mms-server-release:$2" .
  rm -f  mc-mms-server-standalone-*.jar
  rm start.sh
  exit


elif [ "$1" = "push" ]; then  
  if [ -z "$2" ]; then
    echo "No version supplied"
    exit
  fi
  echo "Pushing to docker.io - make sure you are logged in"
  docker push dmadk/mc-mms-server-release:$2
  exit     
else
    echo Unknown target: "$1"
    echo Valid targets are:
fi

echo "  build <version>   Builds a version from cloudbees using the specified version"
echo "  push  <version>   Pushes version to docker.io"

