#!/usr/bin/env bash

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
FS_DST=$CURRENT_DIR/docker/service_fs
MDB_DST=$CURRENT_DIR/docker/service_mongo

cd $FS_DST && docker-compose down
cd $MDB_DST && docker-compose down
#
cd $CURRENT_DIR/code/common-services && mvn clean
#
cd $CURRENT_DIR/code/upload-service && mvn clean
rm $FS_DST/uploader/service/upload-service
rm $MDB_DST/uploader/service/upload-service
#
cd $CURRENT_DIR/code/image-converter && mvn clean
rm $FS_DST/converter/service/image-converter
rm $MDB_DST/converter/service/image-converter
#
cd $CURRENT_DIR/code/image-service && mvn clean
rm $FS_DST/viewer/service/image-service
rm $MDB_DST/viewer/service/image-service
#
echo && echo "all services stopped!"
