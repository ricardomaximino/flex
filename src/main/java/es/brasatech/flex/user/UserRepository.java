package es.brasatech.flex.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {

    // Basic queries for specific fields
    List<User> findByType(String type);
    List<User> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);
}
