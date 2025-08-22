package es.brasatech.flex.data;

import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.ValidationException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data implements Serializable, Flex {

    private String id;
    private String type;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Map<String, Object> customFields;
    public static final List<String> STANDARD_FIELDS = Arrays.asList("id", "creationDate", "updateDate", "type");


    public Data(String type, Map<String, Object> customFields) {
        this.type = type;
        this.customFields = customFields != null ? new HashMap<>(removeStandard(customFields)) : new HashMap<>();
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public void addCustomField(String key, Object value) {
        if (this.customFields == null) {
            this.customFields = new HashMap<>();
        }
        if(STANDARD_FIELDS.contains(key)){
            throw new ValidationException(String.format("The field are not editable it belong to the internal data.%nInternal data [%s]", STANDARD_FIELDS));
        }
        this.customFields.put(key, value);
        this.updateDate = LocalDateTime.now();
    }

    public Object getCustomField(String key) {
        return this.customFields != null ? this.customFields.get(key) : null;
    }

    public boolean hasCustomField(String key) {
        return this.customFields != null && this.customFields.containsKey(key);
    }

    public void removeCustomField(String key) {
        if (this.customFields != null) {
            this.customFields.remove(key);
            this.updateDate = LocalDateTime.now();
        }
    }

    public void updateTimestamp() {
        this.updateDate = LocalDateTime.now();
    }

    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields != null ? new HashMap<>(removeStandard(customFields)) : new HashMap<>();
        this.updateDate = LocalDateTime.now();
    }

    public boolean isValid() {
        return id != null && !id.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", customFieldsCount=" + (customFields != null ? customFields.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(id, data.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> removeStandard(Map<String, Object> data) {
        STANDARD_FIELDS.forEach(data::remove);
        return data;
    }

}