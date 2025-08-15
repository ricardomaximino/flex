package es.brasatech.flex.data;

import es.brasatech.flex.shared.Flex;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "data")
public class Data implements Flex {
    @Id
    private String id;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private String type;

    // This Map will store all dynamic fields
    private Map<String, Object> customFields = new HashMap<>();

    public void prepareToSave() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
            id = UUID.randomUUID().toString().replace("-", "");
        }
        updateDate = LocalDateTime.now();
    }

}