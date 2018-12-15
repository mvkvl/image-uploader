package ws.slink.test.datastore;

import org.springframework.data.mongodb.repository.MongoRepository;

import ws.slink.test.datatype.Base64EncodedImageJson;

@ConditionalOnProperty(name="images.datastore.type", havingValue="mongodb", matchIfMissing=false)
public interface MongoDBImageRepository extends MongoRepository<Base64EncodedImageJson, String> {

}
