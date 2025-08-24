package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.DataService;
import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.ValidatorManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.Profile;

import java.nio.file.Path;

@Configuration
@Profile("standalone")
@ImportRuntimeHints(MapDBHints.class)
public class MapDBConfig {

    @Bean
    public MapDBDataStore mapDBDataStore(@Value("${mapdb.file.path:default}") String dbPath){
        if(dbPath.equals("default")) {
            dbPath = Path.of(System.getProperty("user.home"), ".mapdb-store").toString();
        }
        return new MapDBDataStore(dbPath);
    }

    @Bean
    public SearchDataRepository<MapDBData> mapDBDataSearchDataRepository(MapDBDataStore mapDBDataStore) {
        return new MapDBSearchDataRepositoryImpl(mapDBDataStore);
    }

    @Bean
    public DataService<Data> mapDBDataDataService(MapDBDataStore mapDBDataStore, SearchDataRepository<MapDBData> searchDataRepository, ValidatorManager validatorManager, ApplicationEventPublisher internalPublisher){
        return new MapDBDataServiceImpl(mapDBDataStore, searchDataRepository, validatorManager, internalPublisher);
    }

}
