package es.brasatech.flex.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataRepository extends MongoRepository<Data, String>, CustomDataRepository {

    // Basic queries for specific fields
    List<Data> findByType(String type);
    List<Data> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);
}
