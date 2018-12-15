package ws.slink.test.datastore;

import org.springframework.data.mongodb.repository.MongoRepository;

import ws.slink.test.datatype.Base64EncodedPreviewJson;

@ConditionalOnProperty(name="images.datastore.type", havingValue="mongodb", matchIfMissing=false)
public interface MongoDBPreviewRepository extends MongoRepository<Base64EncodedPreviewJson, String> {

}
