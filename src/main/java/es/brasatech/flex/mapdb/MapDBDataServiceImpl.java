package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.DataService;
import es.brasatech.flex.data.SearchCriteria;
import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.InternalDataEvent;
import es.brasatech.flex.shared.ValidationException;
import es.brasatech.flex.shared.ValidatorManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MapDBDataServiceImpl implements DataService<Data> {

    private final MapDBDataStore dataStore;
    private final SearchDataRepository<MapDBData> searchDataRepository;
    private final ValidatorManager validatorManager;
    private final ApplicationEventPublisher internalPublisher;

    @Override
    public Data save(Map<String, Object> dataMap) {
        String type = (String) dataMap.get("type");
        if(type == null) {
            throw new ValidationException("Type field is a mandatory data");
        }
        MapDBData data = new MapDBData(type, dataMap);
        validatorManager.validate(data);
        var savedData = dataStore.save(data);
        internalPublisher.publishEvent(new InternalDataEvent<Data>(savedData, "New [" + savedData.getType() + "] was created!"));
        return savedData;
    }

    @Override
    public Data update(String id, Map<String, Object> updateData) {
        MapDBData existingData = dataStore.findById(id).orElseThrow(() -> new RuntimeException("Data not found with id: " + id));
        existingData.setCustomFields(updateData);
        validatorManager.validate(existingData);
        return dataStore.save(existingData);
    }

    public List<Data> findAll() {
        return new ArrayList<>(dataStore.findAll());
    }

    public Optional<Data> findById(String id) {
        return dataStore.findById(id).map(MapDBMapper::mapToData);
    }

    public void deleteById(String id) {
        dataStore.deleteById(id);
    }

    @Override
    public List<Data> simpleSearch(Map<String, Object> searchCriteria) {
        log.info("Performing simple search with criteria: {}", searchCriteria);
        return searchDataRepository.findByDynamicSimpleCriteria(searchCriteria).stream().map(MapDBMapper::mapToData).toList();
    }

    @Override
    public List<Data> advancedSearch(Map<String, SearchCriteria> searchCriteria) {
        log.info("Performing advanced search with criteria: {}", searchCriteria);
        return searchDataRepository.findByDynamicAdvancedCriteria(searchCriteria).stream().map(MapDBMapper::mapToData).toList();
    }

    @Override
    public Page<Data> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                                  int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated simple search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return searchDataRepository.findByDynamicSimpleCriteriaWithPaging(searchCriteria, pageable).map(MapDBMapper::mapToData);
    }

    @Override
    public Page<Data> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria,
                                                    int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated advanced search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return searchDataRepository.findByDynamicAdvancedCriteriaWithPaging(searchCriteria, pageable).map(MapDBMapper::mapToData);
    }

    public Data partialUpdate(String id, Map<String, Object> updateFields) {
        MapDBData existingData = dataStore.findById(id)
                .orElseThrow(() -> new RuntimeException("Data not found with id: " + id));

        // Update only the provided fields
        Map<String, Object> currentCustomFields = existingData.getCustomFields();
        if (currentCustomFields == null) {
            currentCustomFields = new HashMap<>();
        }

        for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Skip standard fields
            if (!Data.STANDARD_FIELDS.contains(key)) {
                currentCustomFields.put(key, value);
            }
        }

        existingData.setCustomFields(currentCustomFields);
        existingData.updateTimestamp();

        return dataStore.save(existingData);
    }

    public List<Data> findByType(String type) {
        return dataStore.findByType(type).stream().map(MapDBMapper::mapToData).toList();
    }

    public List<Data> findByCustomField(String fieldName, Object value) {
        return dataStore.findByCustomField(fieldName, value).stream().map(MapDBMapper::mapToData).toList();
    }

    public List<Data> findRecentData(int days) {
        return dataStore.findRecentData(days).stream().map(MapDBMapper::mapToData).toList();
    }

    public void deleteAll() {
        dataStore.deleteAll();
    }

    public long count() {
        return dataStore.count();
    }

    // Batch operations
    public List<Data> saveAll(List<Map<String, Object>> dataList, String type) {
        List<MapDBData> dataObjects = dataList.stream()
            .map(dataMap -> new MapDBData(type, dataMap))
            .collect(Collectors.toList());

        return dataStore.saveAll(dataObjects).stream().map(MapDBMapper::mapToData).toList();
    }

    // Statistics and maintenance
    public Map<String, Object> getStatistics() {
        return dataStore.getStats();
    }

    public void compactDatabase() {
        dataStore.compact();
    }

}