package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
public class MapDBData extends Data {

    private static final long serialVersionUID = 1L;

    public MapDBData(String type, Map<String, Object> customFields) {
        super(type, customFields);
    }

    public MapDBData(String id, String type, Map<String, Object> customFields, LocalDateTime creationDate, LocalDateTime updateDate) {
        super(id, type, creationDate, updateDate, customFields);
    }

}
