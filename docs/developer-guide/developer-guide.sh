#!/bin/bash

if [ "$1" = "pdf" ]; then
   cd adoc
   asciidoctor-pdf -r asciidoctor-diagram developer-guide.adoc -D ../target
   cd ..
   exit
elif [ "$1" = "html" ]; then
   asciidoctor -r asciidoctor-diagram adoc/developer-guide.adoc -D target
   exit
elif [ -z "$1" ]; then 
	echo Usage: $0 target
	echo where target is:
else
	echo Unknown target: "$1"
	echo Valid targets are:
fi

echo "  pdf        Generates documentation in pdf"
echo "  html       Generates documentation in html"

