package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@Document(collection = "data")
public class MongoDBData extends Data {

    public MongoDBData(String type, Map<String, Object> customFields) {
        super(type, customFields);
    }

    public MongoDBData(String id, String type, Map<String, Object> customFields, LocalDateTime creationDate, LocalDateTime updateDate) {
        super(id, type, creationDate, updateDate, customFields);
    }

    @Id
    @Override
    public String getId(){
        return super.getId();
    }
}