package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.DataService;
import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.ValidatorManager;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@Profile("standalone")
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
        return new SearchDataRepositoryImpl(mapDBDataStore);
    }

    @Bean
    public DataService<Data> mapDBDataDataService(MapDBDataStore mapDBDataStore, SearchDataRepository<MapDBData> searchDataRepository, ValidatorManager validatorManager, ApplicationEventPublisher internalPublisher){
        return new MapDBDataServiceImpl(mapDBDataStore, searchDataRepository, validatorManager, internalPublisher);
    }

    public Map<String, Object> createDB(String type) {
        DB dbDisk, dbMemory;
        File file = new File(type);

        dbDisk = DBMaker.fileDB(file).make();
        ConcurrentMap onDisk = dbDisk.hashMap(type).create();
        dbMemory = DBMaker.memoryDB().make();
        ConcurrentMap<String, Object> inMemory = dbMemory.hashMap(type)
               .expireAfterGet(1, TimeUnit.SECONDS)
               .expireOverflow(onDisk)
               .expireExecutor(Executors.newScheduledThreadPool(2))
               .create();
        return inMemory;
    }
}
