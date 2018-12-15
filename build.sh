#!/usr/bin/env bash

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
FS_DST=$CURRENT_DIR/docker/service_fs
MDB_DST=$CURRENT_DIR/docker/service_mongo


cd $CURRENT_DIR/code/common-services && mvn clean install                && \
#
cd $CURRENT_DIR/code/upload-service && mvn clean package                 && \
cp target/upload-service.jar $FS_DST/uploader/service/upload-service     && \
cp target/upload-service.jar $MDB_DST/uploader/service/upload-service    && \
#
cd $CURRENT_DIR/code/image-converter && mvn clean package                && \
cp target/image-converter.jar $FS_DST/converter/service/image-converter  && \
cp target/image-converter.jar $MDB_DST/converter/service/image-converter && \
#
cd $CURRENT_DIR/code/image-service && mvn clean package                  && \
cp target/image-service.jar $FS_DST/viewer/service/image-service         && \
cp target/image-service.jar $MDB_DST/viewer/service/image-service        && \
#
echo && echo "all services prepared!"
