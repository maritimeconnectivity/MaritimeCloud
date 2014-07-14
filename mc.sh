#!/bin/bash
#Any command that fails will exit the script
set -e 

git status
if ! git diff-index --quiet HEAD --; then
  echo "There are uncommitted changes to /MaritimeCloud"
  exit 1
fi


read -p "Enter release version: " release
read -p "Enter Next release version (without snapshot): " next

next="$next-SNAPSHOT"
echo ""
echo "Release version: $release"
echo "Next version: $next"
read -p "Press enter to start release process: " enter

mvn -DperformRelease=true clean source:jar javadoc:jar install

#Lets bump the version counter

mvn versions:set -DnewVersion=$release
mvn -DperformRelease=true clean source:jar javadoc:jar install
mvn versions:commit


#git add
#Commit and tag git


#Build and deploy

#mvn versions:set -DnewVersion=$release
#mvn versions:commit
#