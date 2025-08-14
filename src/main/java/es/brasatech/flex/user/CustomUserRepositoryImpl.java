package es.brasatech.flex.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<User> findByDynamicSimpleCriteria(Map<String, Object> searchCriteria) {
        Query query = buildSimpleQuery(searchCriteria);
        log.debug("Executing simple search query: {}", query);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByDynamicAdvancedCriteria(Map<String, SearchCriteria> searchCriteria) {
        Query query = buildAdvancedQuery(searchCriteria);

        log.debug("Executing advanced search query: {}", query);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public Page<User> findByDynamicSimpleCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable) {
        Query query = buildSimpleQuery(searchCriteria);

        // Get total count before adding pagination
        long total = mongoTemplate.count(Query.of(query).skip(0).limit(0), User.class);

        // Add pagination to query
        query.with(pageable);

        // Get results
        List<User> results = mongoTemplate.find(query, User.class);

        log.debug("Executing paginated search query: {}, Total: {}", query, total);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<User> findByDynamicAdvancedCriteriaWithPaging(Map<String, SearchCriteria> searchCriteria, Pageable pageable) {
        Query query = buildAdvancedQuery(searchCriteria);

        // Get total count before adding pagination
        long total = mongoTemplate.count(Query.of(query).skip(0).limit(0), User.class);

        // Add pagination to query
        query.with(pageable);

        // Get results
        List<User> results = mongoTemplate.find(query, User.class);

        log.debug("Executing paginated search query: {}, Total: {}", query, total);
        return new PageImpl<>(results, pageable, total);
    }

    // Helper method to build simple queries
    private Query buildSimpleQuery(Map<String, Object> searchCriteria) {
        Query query = new Query();

        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return query;
        }

        for (Map.Entry<String, Object> entry : searchCriteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                continue; // Skip null values
            }

            if (isStandardField(key)) {
                addStandardFieldCriteria(query, key, value);
            } else {
                // For custom fields, handle different value types
                if (value instanceof String && ((String) value).contains("*")) {
                    // Support wildcard search with *
                    String regexValue = ((String) value).replace("*", ".*");
                    query.addCriteria(Criteria.where("customFields." + key).regex(regexValue, "i"));
                } else {
                    query.addCriteria(Criteria.where("customFields." + key).is(value));
                }
            }
        }

        return query;
    }

    private Query buildAdvancedQuery(Map<String, SearchCriteria> searchCriteria) {
        Query query = new Query();
        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return query;
        }

        List<Criteria> criteriaList = new ArrayList<>();

        for (Map.Entry<String, SearchCriteria> entry : searchCriteria.entrySet()) {
            String fieldName = entry.getKey();
            SearchCriteria searchCriteriaValue = entry.getValue();

            try {
                Criteria mongoCriteria = buildCriteriaWithOperators(fieldName, searchCriteriaValue);
                if (mongoCriteria != null) {
                    criteriaList.add(mongoCriteria);
                }
            } catch (Exception e) {
                log.error("Error building criteria for field '{}': {}", fieldName, e.getMessage());
                throw new RuntimeException("Invalid search criteria for field: " + fieldName, e);
            }
        }

        if (!criteriaList.isEmpty()) {
            if (criteriaList.size() == 1) {
                query.addCriteria(criteriaList.get(0));
            } else {
                query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            }
        }
        return query;
    }

    private boolean isStandardField(String fieldName) {
        return Arrays.asList("id", "creationDate", "updateDate", "type").contains(fieldName);
    }

    private void addStandardFieldCriteria(Query query, String key, Object value) {
        switch (key) {
            case "id":
                query.addCriteria(Criteria.where("id").is(value));
                break;
            case "type":
                if (value instanceof String && ((String) value).contains("*")) {
                    String regexValue = ((String) value).replace("*", ".*");
                    query.addCriteria(Criteria.where("type").regex(regexValue, "i"));
                } else {
                    query.addCriteria(Criteria.where("type").is(value));
                }
                break;
            case "creationDate":
            case "updateDate":
                if (value instanceof Map) {
                    handleDateRangeCriteria(query, key, (Map<String, Object>) value);
                } else {
                    LocalDateTime dateValue = parseDate(value);
                    if (dateValue != null) {
                        query.addCriteria(Criteria.where(key).is(dateValue));
                    }
                }
                break;
        }
    }

    private void handleDateRangeCriteria(Query query, String fieldName, Map<String, Object> dateRange) {
        Criteria criteria = Criteria.where(fieldName);
        boolean hasCondition = false;

        if (dateRange.containsKey("from") && dateRange.get("from") != null) {
            LocalDateTime fromDate = parseDate(dateRange.get("from"));
            if (fromDate != null) {
                criteria.gte(fromDate);
                hasCondition = true;
            }
        }

        if (dateRange.containsKey("to") && dateRange.get("to") != null) {
            LocalDateTime toDate = parseDate(dateRange.get("to"));
            if (toDate != null) {
                criteria.lte(toDate);
                hasCondition = true;
            }
        }

        if (hasCondition) {
            query.addCriteria(criteria);
        }
    }

    private LocalDateTime parseDate(Object dateValue) {
        if (dateValue == null) {
            return null;
        }

        try {
            if (dateValue instanceof String) {
                String dateStr = (String) dateValue;

                // Handle different date formats
                if (dateStr.contains("T")) {
                    // ISO format: 2024-01-15T10:30:00
                    return LocalDateTime.parse(dateStr);
                } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    // Date only: 2024-01-15
                    return LocalDate.parse(dateStr).atStartOfDay();
                } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    // Format: 2024-01-15 10:30:00
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return LocalDateTime.parse(dateStr, formatter);
                }

                // Try to parse as ISO instant and convert
                return LocalDateTime.parse(dateStr);

            } else if (dateValue instanceof LocalDateTime) {
                return (LocalDateTime) dateValue;
            } else if (dateValue instanceof LocalDate) {
                return ((LocalDate) dateValue).atStartOfDay();
            } else if (dateValue instanceof Date) {
                return ((Date) dateValue).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            }
        } catch (Exception e) {
            log.warn("Failed to parse date: {}, error: {}", dateValue, e.getMessage());
        }

        throw new IllegalArgumentException("Invalid date format: " + dateValue);
    }

    private Criteria buildCriteriaWithOperators(String fieldName, SearchCriteria searchCriteria) {
        String fullFieldName = isStandardField(fieldName) ? fieldName : "customFields." + fieldName;
        Object value = searchCriteria.getValue();

        log.debug("Building criteria for field '{}' (mapped to '{}') with operation '{}' and value '{}'",
                fieldName, fullFieldName, searchCriteria.getOperation(), value);

        // Handle null values
        if (value == null && searchCriteria.getOperation() != SearchOperation.EXISTS) {
            return Criteria.where(fullFieldName).isNull();
        }

        switch (searchCriteria.getOperation()) {
            case EQUALS:
                return Criteria.where(fullFieldName).is(value);

            case NOT_EQUALS:
                return Criteria.where(fullFieldName).ne(value);

            case GREATER_THAN:
                return Criteria.where(fullFieldName).gt(convertValueForComparison(value));

            case GREATER_THAN_EQUAL:
                return Criteria.where(fullFieldName).gte(convertValueForComparison(value));

            case LESS_THAN:
                return Criteria.where(fullFieldName).lt(convertValueForComparison(value));

            case LESS_THAN_EQUAL:
                return Criteria.where(fullFieldName).lte(convertValueForComparison(value));

            case CONTAINS:
                if (value instanceof String) {
                    return Criteria.where(fullFieldName).regex(Pattern.quote((String) value), "i");
                }
                return Criteria.where(fullFieldName).is(value);

            case STARTS_WITH:
                if (value instanceof String) {
                    return Criteria.where(fullFieldName).regex("^" + Pattern.quote((String) value), "i");
                }
                return Criteria.where(fullFieldName).is(value);

            case ENDS_WITH:
                if (value instanceof String) {
                    return Criteria.where(fullFieldName).regex(Pattern.quote((String) value) + "$", "i");
                }
                return Criteria.where(fullFieldName).is(value);

            case IN:
                Collection<?> inValues = convertToCollection(value);
                return Criteria.where(fullFieldName).in(inValues);

            case NOT_IN:
                Collection<?> ninValues = convertToCollection(value);
                return Criteria.where(fullFieldName).nin(ninValues);

            case EXISTS:
                Boolean exists = (Boolean) value;
                return Criteria.where(fullFieldName).exists(exists);

            case BETWEEN:
                return handleBetweenOperation(fullFieldName, value);

            case REGEX:
                if (value instanceof String) {
                    return Criteria.where(fullFieldName).regex((String) value, "i");
                }
                throw new IllegalArgumentException("REGEX operation requires String value");

            default:
                log.warn("Unknown search operation: {}, defaulting to EQUALS", searchCriteria.getOperation());
                return Criteria.where(fullFieldName).is(value);
        }
    }

    private Collection<?> convertToCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection<?>) value;
        } else if (value instanceof Object[]) {
            return Arrays.asList((Object[]) value);
        } else {
            return Collections.singletonList(value);
        }
    }

    private Criteria handleBetweenOperation(String fullFieldName, Object value) {
        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("BETWEEN operation requires a Map with 'from' and/or 'to' keys");
        }

        Map<String, Object> range = (Map<String, Object>) value;
        Object from = range.get("from");
        Object to = range.get("to");

        Criteria criteria = Criteria.where(fullFieldName);

        if (from != null && to != null) {
            return criteria.gte(convertValueForComparison(from)).lte(convertValueForComparison(to));
        } else if (from != null) {
            return criteria.gte(convertValueForComparison(from));
        } else if (to != null) {
            return criteria.lte(convertValueForComparison(to));
        } else {
            throw new IllegalArgumentException("BETWEEN operation requires at least 'from' or 'to' value");
        }
    }

    private Object convertValueForComparison(Object value) {
        // Convert string dates to LocalDateTime for comparison
        if (value instanceof String && ((String) value).matches("\\d{4}-\\d{2}-\\d{2}.*")) {
            try {
                return parseDate(value);
            } catch (Exception e) {
                // If parsing fails, return original value
                return value;
            }
        }
        return value;
    }
}