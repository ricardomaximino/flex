package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
public interface MongoDBDataRepository extends MongoRepository<MongoDBData, String>, DataRepository<MongoDBData> {

}
