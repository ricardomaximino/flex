package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.ValidatorManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("prod")
@Configuration
public class MongoDBConfig {

    @Bean
    public SearchDataRepository<MongoDBData> mongoDBDataSearchDataRepository(MongoTemplate mongoTemplate) {
        return new SearchDataRepositoryImpl(mongoTemplate);
    }

    @Bean
    public DataServiceImpl mongoDBDataService(MongoDBDataRepository dataRepository, ValidatorManager validatorManager, ApplicationEventPublisher internalPublisher, SearchDataRepository<MongoDBData> searchDataRepository) {
        return new DataServiceImpl(dataRepository, validatorManager, internalPublisher, searchDataRepository);
    }


}
