package es.brasatech.flex.data;

import java.util.HashMap;
import java.util.Map;

public class RawMapper {

    public static Map<String, Object> map(Data data){
        if(data == null) {
            return null;
        }

        Map<String, Object> map = null;
        if(data.getCustomFields() == null){
            map = new HashMap<String, Object>();
        } else {
            map = new HashMap<String, Object>(data.getCustomFields());

        }
        map.put("id", data.getId());
        map.put("creationDate", data.getCreationDate());
        map.put("updateDate", data.getUpdateDate());
        map.put("type", data.getType());
        return map;
    }

}
