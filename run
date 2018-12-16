#!/usr/bin/env bash

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
FS_DST=$CURRENT_DIR/docker/service_fs
MDB_DST=$CURRENT_DIR/docker/service_mongo

print_usage() {
   echo
   echo "Usage:"
   echo "  ./run.sh -t {fs | mongo} [-l]"
   echo
   exit 1
}

LOGS=0

while getopts t:l option
do
case "${option}"
in
  t) TYPE=${OPTARG};;
  l) LOGS=1;;
  *) print_usage
esac
done


case "${TYPE}"
in
     fs) echo "starting file-system backed image upload service"
         cd $FS_DST
     ;;
  mongo) echo "starting mongodb backed image upload service"
         cd $MDB_DST
     ;;
      *) print_usage
         # echo "incorrect datastore type: $TYPE"
         exit 1
     ;;
esac

docker-compose up -d
echo && echo "services started!" && echo "wait for about 1 minute to let them get up and running"

if [ $LOGS -eq 1 ]; then
   sleep 1
   docker-compose logs -f uploader converter viewer
fi
