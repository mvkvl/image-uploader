#!/usr/bin/env bash

IMGPATH="../../test-images/"
IMAGES="$IMGPATH/img010.jpg $IMGPATH/img011.png"

N=$1

if [ -z $N ]; then
   N=1
fi

for i in $(seq 0 $N); do
   (./test_base64.sh $IMAGES | jq "." ) &
   (./test_multipart.sh $IMAGES | jq ".") &
   (./test_url.sh | jq ".") &
done
