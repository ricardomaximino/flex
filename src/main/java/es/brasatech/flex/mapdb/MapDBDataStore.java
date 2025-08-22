package es.brasatech.flex.mapdb;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class MapDBDataStore {

    private final DB db;
    private final HTreeMap<String, MapDBData> dataMap;

    public MapDBDataStore(String dbPath) {
        log.info("Initializing MapDB with path: {}", dbPath);

        // Create directory if it doesn't exist
        File dbFile = new File(dbPath);
        dbFile.getParentFile().mkdirs();

        // Initialize MapDB
        this.db = DBMaker
                .fileDB(dbPath)
                .checksumHeaderBypass() // For performance
                .closeOnJvmShutdown()
                .make();

        // Create or get the map
        this.dataMap = db.hashMap("data")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .createOrOpen();

        log.info("MapDB initialized successfully with {} existing records", dataMap.size());
    }

    public MapDBData save(MapDBData data) {
        if (data.getId() == null) {
            data.setId(UUID.randomUUID().toString().replace("-", ""));
        }

        if (data.getCreationDate() == null) {
            data.setCreationDate(LocalDateTime.now());
        }
        data.setUpdateDate(LocalDateTime.now());

        dataMap.put(data.getId(), data);
        db.commit(); // Ensure data is persisted

        log.debug("Saved data with ID: {}", data.getId());
        return data;
    }

    public Optional<MapDBData> findById(String id) {
        MapDBData data = dataMap.get(id);
        return Optional.ofNullable(data);
    }

    public Collection<MapDBData> findAll() {
        return new ArrayList<>(dataMap.values());
    }

    public List<MapDBData> findByType(String type) {
        return dataMap.values().stream()
                .filter(data -> Objects.equals(data.getType(), type))
                .collect(Collectors.toList());
    }

    public boolean existsById(String id) {
        return dataMap.containsKey(id);
    }

    public void deleteById(String id) {
        MapDBData removed = dataMap.remove(id);
        if (removed != null) {
            db.commit();
            log.debug("Deleted data with ID: {}", id);
        } else {
            log.warn("Attempted to delete non-existent data with ID: {}", id);
        }
    }

    public void deleteAll() {
        int size = dataMap.size();
        dataMap.clear();
        db.commit();
        log.info("Deleted all {} records from MapDB", size);
    }

    public long count() {
        return dataMap.size();
    }

    public List<MapDBData> findByCustomField(String fieldName, Object value) {
        return dataMap.values().stream()
                .filter(data -> {
                    if (data.getCustomFields() == null) {
                        return false;
                    }
                    Object fieldValue = data.getCustomFields().get(fieldName);
                    return Objects.equals(fieldValue, value);
                })
                .collect(Collectors.toList());
    }

    public List<MapDBData> findRecentData(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return dataMap.values().stream()
                .filter(data -> data.getCreationDate() != null && data.getCreationDate().isAfter(cutoff))
                .collect(Collectors.toList());
    }

    // Batch operations
    public List<MapDBData> saveAll(List<MapDBData> dataList) {
        List<MapDBData> savedData = new ArrayList<>();

        for (MapDBData data : dataList) {
            savedData.add(save(data));
        }

        log.info("Batch saved {} records", savedData.size());
        return savedData;
    }

    public void deleteAllById(List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            if (dataMap.remove(id) != null) {
                deletedCount++;
            }
        }

        if (deletedCount > 0) {
            db.commit();
            log.info("Batch deleted {} records", deletedCount);
        }
    }

    // Statistics and maintenance
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", dataMap.size());
        stats.put("dbSize", StreamSupport.stream(db.getStore().getAllFiles().spliterator(), false).count());

        // Count by type
        Map<String, Long> typeCount = dataMap.values().stream()
                .collect(Collectors.groupingBy(
                        data -> data.getType() != null ? data.getType() : "UNKNOWN",
                        Collectors.counting()));
        stats.put("recordsByType", typeCount);

        // Recent activity (last 7 days)
        long recentCount = findRecentData(7).size();
        stats.put("recentRecords", recentCount);

        return stats;
    }

    public void compact() {
        log.info("Compacting MapDB database...");
//        db.compact();
        log.info("Database compacted successfully");
    }

    @PreDestroy
    public void close() {
        if (db != null && !db.isClosed()) {
            log.info("Closing MapDB database...");
            db.commit();
            db.close();
            log.info("MapDB database closed successfully");
        }
    }
}