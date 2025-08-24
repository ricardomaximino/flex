package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.DataService;
import es.brasatech.flex.data.SearchCriteria;
import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.shared.InternalDataEvent;
import es.brasatech.flex.shared.NotFoundException;
import es.brasatech.flex.shared.ValidationException;
import es.brasatech.flex.shared.ValidatorManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class MongoDBDataServiceImpl implements DataService<Data> {

    private final MongoDBDataRepository dataRepository;
    private final ValidatorManager validatorManager;
    private final ApplicationEventPublisher internalPublisher;
    private final SearchDataRepository<MongoDBData> searchDataRepository;

    @Override
    public Data save(Map<String, Object> dataInfo) {
        String type = (String) dataInfo.get("type");
        if(type == null) {
            throw new ValidationException("Type field is a mandatory data");
        }
        Data data = new Data(type, dataInfo);
        validatorManager.validate(data);
        var savedDataDB = dataRepository.save(MongoDBMapper.mapToMongoDBData(data));
        var savedData = MongoDBMapper.mapToData(savedDataDB);
        internalPublisher.publishEvent(new InternalDataEvent<Data>(savedData, "New [" + savedData.getType() + "] was created!"));
        return savedData;
    }

    @Override
    public Data update(String id, Map<String, Object> updateData) {
        Data existingData = dataRepository.findById(id).orElseThrow(() -> new NotFoundException("Data not found with id: " + id));
        existingData.setCustomFields(updateData);
        validatorManager.validate(existingData);
        var savedDataDB = dataRepository.save(MongoDBMapper.mapToMongoDBData(existingData));
        return MongoDBMapper.mapToData(savedDataDB);
    }

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll().stream().map(MongoDBMapper::mapToData).toList();
    }

    @Override
    public Optional<Data> findById(String id) {
        return dataRepository.findById(id).map(MongoDBMapper::mapToData);
    }

    @Override
    public void deleteById(String id) {
        dataRepository.deleteById(id);
    }

    @Override
    public List<Data> simpleSearch(Map<String, Object> searchCriteria) {
        log.info("Performing simple search with criteria: {}", searchCriteria);
        return searchDataRepository.findByDynamicSimpleCriteria(searchCriteria).stream().map(MongoDBMapper::mapToData).toList();
    }

    @Override
    public List<Data> advancedSearch(Map<String, SearchCriteria> searchCriteria) {
        log.info("Performing advanced search with criteria: {}", searchCriteria);
        return searchDataRepository.findByDynamicAdvancedCriteria(searchCriteria).stream().map(MongoDBMapper::mapToData).toList();
    }

    @Override
    public Page<Data> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                             int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated simple search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return searchDataRepository.findByDynamicSimpleCriteriaWithPaging(searchCriteria, pageable).map(MongoDBMapper::mapToData);
    }

    @Override
    public Page<Data> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria, int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated advanced search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return searchDataRepository.findByDynamicAdvancedCriteriaWithPaging(searchCriteria, pageable).map(MongoDBMapper::mapToData);
    }

}
