package es.brasatech.flex.shared;

import java.time.LocalDateTime;
import java.util.Map;

public interface Flex {

    void setId(String id);
    String getId();

    void setType(String type);
    String getType();

    void setCreationDate(LocalDateTime creationDate);
    LocalDateTime getCreationDate();

    void setUpdateDate(LocalDateTime updateDate);
    LocalDateTime getUpdateDate();

    void setCustomFields(Map<String, Object> customFields);
    Map<String, Object> getCustomFields();

}
