#/usr/bin/env bash

SERVICE_URL=http://localhost:7070/images


FILES=""
for file in $@; do
  FILES="$FILES -F file=@$file"
done

curl \
     --request POST \
     $FILES \
     $SERVICE_URL
