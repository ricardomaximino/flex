package es.brasatech.flex.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;

    @Field("creation_date")
    private LocalDateTime creationDate;

    @Field("update_date")
    private LocalDateTime updateDate;

    private String type;

    // This Map will store all dynamic fields
    @Field("custom_fields")
    private Map<String, Object> customFields = new HashMap<>();

    public void save() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
            id = UUID.randomUUID().toString().replace("-", "");
        }
        updateDate = LocalDateTime.now();
    }

}