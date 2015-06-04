#!/bin/bash

# Artifactory location
name=$1
release=$2
url=https://oss.sonatype.org/content/repositories/releases/net/maritimecloud/mms/$name/$release/$name-$release.jar

# Clean up old versions
rm -f $name-*.jar

# Download
echo "Downloading build $url"
curl -o $name-$release.jar $url

