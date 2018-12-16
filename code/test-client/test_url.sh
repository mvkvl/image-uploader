

SERVICE_URL=http://localhost:7070/upload


curl --silent \
     --header "Content-Type: application/x-www-form-urlencoded" \
     --request POST \
     -d "url=http://krasivie-kartinki.ru/images/noviy-god15.jpg" \
     -d "url=http://www.crazyshark.ru/wp-content/uploads/2010/01/ded-moroz-01.jpg" \
     $SERVICE_URL | jq "."
