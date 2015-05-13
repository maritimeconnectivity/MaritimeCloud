#!/bin/bash

# Artifactory location
name=$1
release=$2
path=https://oss.sonatype.org/content/repositories/snapshots/net/maritimecloud/mms/$name/$release/

# Clean up old versions
rm -f $name-*.jar

# Maven artifact location
version=`curl -s $path/maven-metadata.xml | grep latest | sed "s/.*<latest>\([^<]*\)<\/latest>.*/\1/"`
build=`curl -s $path/$version/maven-metadata.xml | grep '<value>' | head -1 | sed "s/.*<value>\([^<]*\)<\/value>.*/\1/"`
jar=$name-$build.jar
url=$path/$version/$jar

# Download
echo "Downloading build $url"
curl -o $name-$release.jar $url

