#/usr/bin/env bash

SERVICE_URL=http://localhost:7070/upload



shopt -s nocasematch
get_mime_type() {
  case "$1" in
  "jpg") echo "image/jpeg";;
  "jpeg") echo "image/jpeg";;
  "gif") echo "image/gif";;
  "png") echo "image/png";;
  *) echo "";;
  esac
}  
jsonify_file() {
  filename=$(basename -- "$file")
  extension="${filename##*.}"
  filename="${filename%.*}"
  type=`get_mime_type $extension`
  bs64=`base64 $file`
  echo "{\"name\":\"$(basename $file)\",\"type\":\"$type\",\"base64\":\"$bs64\"}" 
}

JSON="["
for file in $@; do
  JSON="$JSON$(jsonify_file "$file"),"
done
JSON="${JSON%?}]"

curl --silent \
     --header "Content-Type: application/json" \
     --request POST \
     --data $JSON \
     $SERVICE_URL | jq "."
