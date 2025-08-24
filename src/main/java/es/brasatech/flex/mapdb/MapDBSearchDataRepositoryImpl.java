package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.SearchCriteria;
import es.brasatech.flex.data.SearchDataRepository;
import es.brasatech.flex.data.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MapDBSearchDataRepositoryImpl implements SearchDataRepository<MapDBData> {

    private final MapDBDataStore dataStore; // Assumes you have a MapDB data store component

    @Override
    public List<MapDBData> findByDynamicSimpleCriteria(Map<String, Object> searchCriteria) {
        log.debug("Performing simple search with criteria: {}", searchCriteria);

        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return new ArrayList<>(dataStore.findAll());
        }

        return dataStore.findAll().stream()
                .filter(data -> matchesSimpleCriteria(data, searchCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public List<MapDBData> findByDynamicAdvancedCriteria(Map<String, SearchCriteria> searchCriteria) {
        log.debug("Performing advanced search with criteria: {}", searchCriteria);

        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return new ArrayList<>(dataStore.findAll());
        }

        return dataStore.findAll().stream()
                .filter(data -> matchesAdvancedCriteria(data, searchCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MapDBData> findByDynamicSimpleCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable) {
        log.debug("Performing paginated simple search with criteria: {}, page: {}", searchCriteria, pageable);

        List<MapDBData> allResults = findByDynamicSimpleCriteria(searchCriteria);

        return createPageFromList(allResults, pageable);
    }

    @Override
    public Page<MapDBData> findByDynamicAdvancedCriteriaWithPaging(Map<String, SearchCriteria> searchCriteria, Pageable pageable) {
        log.debug("Performing paginated advanced search with criteria: {}, page: {}", searchCriteria, pageable);

        List<MapDBData> allResults = findByDynamicAdvancedCriteria(searchCriteria);

        return createPageFromList(allResults, pageable);
    }

    private boolean matchesSimpleCriteria(Data data, Map<String, Object> searchCriteria) {
        for (Map.Entry<String, Object> entry : searchCriteria.entrySet()) {
            String fieldName = entry.getKey();
            Object expectedValue = entry.getValue();

            if (expectedValue == null) {
                continue; // Skip null criteria
            }

            Object actualValue = getFieldValue(data, fieldName);

            // Handle date range for standard fields
            if (isStandardField(fieldName) && expectedValue instanceof Map) {
                if (!matchesDateRange(actualValue, (Map<String, Object>) expectedValue)) {
                    return false;
                }
            } else if (expectedValue instanceof String && ((String) expectedValue).contains("*")) {
                // Handle wildcard search
                if (!matchesWildcard(actualValue, (String) expectedValue)) {
                    return false;
                }
            } else {
                // Exact match
                if (!objectsEqual(actualValue, expectedValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean matchesAdvancedCriteria(Data data, Map<String, SearchCriteria> searchCriteria) {
        for (Map.Entry<String, SearchCriteria> entry : searchCriteria.entrySet()) {
            String fieldName = entry.getKey();
            SearchCriteria criteria = entry.getValue();

            Object actualValue = getFieldValue(data, fieldName);

            if (!matchesSingleCriteria(actualValue, criteria)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesSingleCriteria(Object actualValue, SearchCriteria criteria) {
        Object expectedValue = criteria.getValue();
        SearchOperation operation = criteria.getOperation();

        log.trace("Comparing actual: {} with expected: {} using operation: {}", actualValue, expectedValue, operation);

        switch (operation) {
            case EQUALS:
                return objectsEqual(actualValue, expectedValue);

            case NOT_EQUALS:
                return !objectsEqual(actualValue, expectedValue);

            case GREATER_THAN:
                return compareValues(actualValue, expectedValue) > 0;

            case GREATER_THAN_EQUAL:
                return compareValues(actualValue, expectedValue) >= 0;

            case LESS_THAN:
                return compareValues(actualValue, expectedValue) < 0;

            case LESS_THAN_EQUAL:
                return compareValues(actualValue, expectedValue) <= 0;

            case CONTAINS:
                return actualValue != null && expectedValue != null &&
                        actualValue.toString().toLowerCase().contains(expectedValue.toString().toLowerCase());

            case STARTS_WITH:
                return actualValue != null && expectedValue != null &&
                        actualValue.toString().toLowerCase().startsWith(expectedValue.toString().toLowerCase());

            case ENDS_WITH:
                return actualValue != null && expectedValue != null &&
                        actualValue.toString().toLowerCase().endsWith(expectedValue.toString().toLowerCase());

            case IN:
                return isValueInCollection(actualValue, expectedValue);

            case NOT_IN:
                return !isValueInCollection(actualValue, expectedValue);

            case EXISTS:
                boolean shouldExist = (Boolean) expectedValue;
                return shouldExist ? (actualValue != null) : (actualValue == null);

            case BETWEEN:
                return matchesBetweenCriteria(actualValue, expectedValue);

            case REGEX:
                return matchesRegex(actualValue, (String) expectedValue);

            default:
                log.warn("Unknown search operation: {}, defaulting to EQUALS", operation);
                return objectsEqual(actualValue, expectedValue);
        }
    }

    private Object getFieldValue(Data data, String fieldName) {
        if (isStandardField(fieldName)) {
            switch (fieldName) {
                case "id":
                    return data.getId();
                case "creationDate":
                    return data.getCreationDate();
                case "updateDate":
                    return data.getUpdateDate();
                case "type":
                    return data.getType();
                default:
                    return null;
            }
        } else {
            // Get from custom fields
            String[] fieldNames = fieldName.split("\\.");
            Object obj = data.getCustomFields();
            if(obj == null) {
                return null;
            }
            for (String name : fieldNames) {
                if (obj instanceof Map objMap){
                    obj = ((Map<String, Object>) objMap).get(name);
                }
            }
            return obj;
        }
    }

    private boolean isStandardField(String fieldName) {
        return Arrays.asList("id", "creationDate", "updateDate", "type").contains(fieldName);
    }

    private boolean objectsEqual(Object actual, Object expected) {
        if (actual == null && expected == null) {
            return true;
        }
        if (actual == null || expected == null) {
            return false;
        }

        // Handle different number types
        if (actual instanceof Number && expected instanceof Number) {
            return ((Number) actual).doubleValue() == ((Number) expected).doubleValue();
        }

        // Handle string comparison (case-insensitive)
        if (actual instanceof String && expected instanceof String) {
            return ((String) actual).equalsIgnoreCase((String) expected);
        }

        return actual.equals(expected);
    }

    private int compareValues(Object actual, Object expected) {
        if (actual == null && expected == null) {
            return 0;
        }
        if (actual == null) {
            return -1;
        }
        if (expected == null) {
            return 1;
        }

        // Handle numbers
        if (actual instanceof Number && expected instanceof Number) {
            return Double.compare(((Number) actual).doubleValue(), ((Number) expected).doubleValue());
        }

        // Handle dates
        if (actual instanceof LocalDateTime && expected instanceof LocalDateTime) {
            return ((LocalDateTime) actual).compareTo((LocalDateTime) expected);
        }

        if (actual instanceof LocalDate && expected instanceof LocalDate) {
            return ((LocalDate) actual).compareTo((LocalDate) expected);
        }

        // Handle strings
        if (actual instanceof String && expected instanceof String) {
            return ((String) actual).compareToIgnoreCase((String) expected);
        }

        // Handle Comparable objects
        if (actual instanceof Comparable && expected instanceof Comparable) {
            try {
                return ((Comparable) actual).compareTo(expected);
            } catch (ClassCastException e) {
                log.warn("Cannot compare {} with {}", actual.getClass(), expected.getClass());
                return actual.toString().compareToIgnoreCase(expected.toString());
            }
        }

        // Fallback to string comparison
        return actual.toString().compareToIgnoreCase(expected.toString());
    }

    private boolean isValueInCollection(Object actualValue, Object expectedCollection) {
        if (actualValue == null) {
            return false;
        }

        Collection<?> collection = convertToCollection(expectedCollection);

        return collection.stream()
                .anyMatch(item -> objectsEqual(actualValue, item));
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

    private boolean matchesBetweenCriteria(Object actualValue, Object expectedValue) {
        if (!(expectedValue instanceof Map)) {
            return false;
        }

        Map<String, Object> range = (Map<String, Object>) expectedValue;
        Object from = range.get("from");
        Object to = range.get("to");

        if (from != null) {
            if (compareValues(actualValue, from) < 0) {
                return false;
            }
        }

        if (to != null) {
            if (compareValues(actualValue, to) > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesRegex(Object actualValue, String pattern) {
        if (actualValue == null || pattern == null) {
            return false;
        }

        try {
            Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            return compiledPattern.matcher(actualValue.toString()).find();
        } catch (PatternSyntaxException e) {
            log.warn("Invalid regex pattern: {}", pattern);
            return false;
        }
    }

    private boolean matchesWildcard(Object actualValue, String pattern) {
        if (actualValue == null || pattern == null) {
            return false;
        }

        // Convert wildcard pattern to regex
        String regexPattern = pattern.replace("*", ".*");
        return matchesRegex(actualValue, regexPattern);
    }

    private boolean matchesDateRange(Object actualValue, Map<String, Object> dateRange) {
        if (!(actualValue instanceof LocalDateTime)) {
            return false;
        }

        LocalDateTime actualDate = (LocalDateTime) actualValue;

        Object from = dateRange.get("from");
        Object to = dateRange.get("to");

        if (from != null) {
            LocalDateTime fromDate = parseDate(from);
            if (fromDate != null && actualDate.isBefore(fromDate)) {
                return false;
            }
        }

        if (to != null) {
            LocalDateTime toDate = parseDate(to);
            if (toDate != null && actualDate.isAfter(toDate)) {
                return false;
            }
        }

        return true;
    }

    private LocalDateTime parseDate(Object dateValue) {
        if (dateValue == null) {
            return null;
        }

        try {
            if (dateValue instanceof String) {
                String dateStr = (String) dateValue;

                if (dateStr.contains("T")) {
                    return LocalDateTime.parse(dateStr);
                } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return LocalDate.parse(dateStr).atStartOfDay();
                } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return LocalDateTime.parse(dateStr, formatter);
                }

                return LocalDateTime.parse(dateStr);

            } else if (dateValue instanceof LocalDateTime) {
                return (LocalDateTime) dateValue;
            } else if (dateValue instanceof LocalDate) {
                return ((LocalDate) dateValue).atStartOfDay();
            }
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateValue);
        }

        return null;
    }

    private Page<MapDBData> createPageFromList(List<MapDBData> allResults, Pageable pageable) {
        // Sort the results
        if (pageable.getSort().isSorted()) {
            allResults = sortResults(allResults, pageable.getSort());
        }

        // Calculate pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());

        List<MapDBData> pageContent = new ArrayList<>();
        if (start < allResults.size()) {
            pageContent = allResults.subList(start, end);
        }

        return new PageImpl<>(pageContent, pageable, allResults.size());
    }

    private List<MapDBData> sortResults(List<MapDBData> results, Sort sort) {
        return results.stream()
                .sorted((data1, data2) -> {
                    for (Sort.Order order : sort) {
                        String property = order.getProperty();
                        Object value1 = getFieldValue(data1, property);
                        Object value2 = getFieldValue(data2, property);

                        int comparison = compareValues(value1, value2);

                        if (comparison != 0) {
                            return order.isAscending() ? comparison : -comparison;
                        }
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }
}