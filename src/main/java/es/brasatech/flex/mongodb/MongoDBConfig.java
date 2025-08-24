package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.ValidatorManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("prod")
public class MongoDBConfig {

    @Bean
    public SearchDataRepository<MongoDBData> mongoDBDataSearchDataRepository(MongoTemplate mongoTemplate) {
        return new MongoDBSearchDataRepositoryImpl(mongoTemplate);
    }

    @Bean
    public MongoDBDataServiceImpl mongoDBDataService(MongoDBDataRepository dataRepository, ValidatorManager validatorManager, ApplicationEventPublisher internalPublisher, SearchDataRepository<MongoDBData> searchDataRepository) {
        return new MongoDBDataServiceImpl(dataRepository, validatorManager, internalPublisher, searchDataRepository);
    }

}
