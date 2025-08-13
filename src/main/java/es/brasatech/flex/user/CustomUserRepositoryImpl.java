package es.brasatech.flex.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final MongoTemplate mongoTemplate;



    @Override
    public List<User> findByDynamicCriteriaWithOperators(Map<String, SearchCriteria> searchCriteria) {
        Query query = new Query();

        for (Map.Entry<String, SearchCriteria> entry : searchCriteria.entrySet()) {
            String fieldName = entry.getKey();
            SearchCriteria criteria = entry.getValue();

            Criteria mongoCriteria = buildCriteriaWithOperators(fieldName, criteria);
            query.addCriteria(mongoCriteria);
        }

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public Page<User> findByDynamicCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable) {
        Query query = new Query();

        // Build criteria
        for (Map.Entry<String, Object> entry : searchCriteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (isStandardField(key)) {
                addStandardFieldCriteria(query, key, value);
            } else {
                query.addCriteria(Criteria.where("customFields." + key).is(value));
            }
        }

        // Add pagination
        query.with(pageable);

        // Get total count
        long total = mongoTemplate.count(query.skip(0).limit(0), User.class);

        // Get results
        List<User> results = mongoTemplate.find(query, User.class);

        return new PageImpl<>(results, pageable, total);
    }

    private boolean isStandardField(String fieldName) {
        return Arrays.asList("id", "creationDate", "updateDate").contains(fieldName);
    }

    private void addStandardFieldCriteria(Query query, String key, Object value) {
        switch (key) {
            case "id":
                query.addCriteria(Criteria.where("id").is(value));
                break;
            case "creationDate":
            case "updateDate":
                if (value instanceof Map) {
                    handleDateRangeCriteria(query, key, (Map<String, Object>) value);
                } else {
                    query.addCriteria(Criteria.where(key).is(value));
                }
                break;
        }
    }

    private void handleDateRangeCriteria(Query query, String fieldName, Map<String, Object> dateRange) {
        Criteria criteria = Criteria.where(fieldName);

        if (dateRange.containsKey("from")) {
            criteria.gte(parseDate(dateRange.get("from")));
        }
        if (dateRange.containsKey("to")) {
            criteria.lte(parseDate(dateRange.get("to")));
        }

        query.addCriteria(criteria);
    }

    private LocalDateTime parseDate(Object dateValue) {
        if (dateValue instanceof String) {
            return LocalDateTime.parse((String) dateValue);
        } else if (dateValue instanceof LocalDateTime) {
            return (LocalDateTime) dateValue;
        }
        throw new IllegalArgumentException("Invalid date format");
    }

    private Criteria buildCriteriaWithOperators(String fieldName, SearchCriteria searchCriteria) {
        String fullFieldName = isStandardField(fieldName) ? fieldName : "customFields." + fieldName;
        Criteria criteria = Criteria.where(fullFieldName);

        switch (searchCriteria.getOperation()) {
            case EQUALS:
                return criteria.is(searchCriteria.getValue());
            case NOT_EQUALS:
                return criteria.ne(searchCriteria.getValue());
            case GREATER_THAN:
                return criteria.gt(searchCriteria.getValue());
            case GREATER_THAN_EQUAL:
                return criteria.gte(searchCriteria.getValue());
            case LESS_THAN:
                return criteria.lt(searchCriteria.getValue());
            case LESS_THAN_EQUAL:
                return criteria.lte(searchCriteria.getValue());
            case CONTAINS:
                return criteria.regex((String) searchCriteria.getValue(), "i");
            case IN:
                return criteria.in((Collection<?>) searchCriteria.getValue());
            case NOT_IN:
                return criteria.nin((Collection<?>) searchCriteria.getValue());
            case EXISTS:
                return criteria.exists((Boolean) searchCriteria.getValue());
            case BETWEEN:
                Map<String, Object> range = (Map<String, Object>) searchCriteria.getValue();
                return criteria.gte(range.get("from")).lte(range.get("to"));
            default:
                return criteria.is(searchCriteria.getValue());
        }
    }
}