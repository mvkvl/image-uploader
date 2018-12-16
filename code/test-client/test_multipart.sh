#/usr/bin/env bash

SERVICE_URL=http://localhost:7070/upload


FILES=""
for file in $@; do
  FILES="$FILES -F file=@$file"
done

curl --silent \
     --request POST \
     $FILES \
     $SERVICE_URL | jq "."
