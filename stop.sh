#!/usr/bin/env bash

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
FS_DST=$CURRENT_DIR/docker/service_fs
MDB_DST=$CURRENT_DIR/docker/service_mongo

print_usage() {
   echo
   echo "Usage:"
   echo "  ./stop.sh -t {fs | mongo}"
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
     fs) echo "stopping file-system backed image upload service"
         cd $FS_DST
     ;;
  mongo) echo "stopping mongodb backed image upload service"
         cd $MDB_DST
     ;;
      *) print_usage
         # echo "incorrect datastore type: $TYPE"
         exit 1
     ;;
esac

docker-compose down
echo && echo "services stoped!"
