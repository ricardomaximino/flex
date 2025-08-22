package es.brasatech.flex.mapdb;

import es.brasatech.flex.data.Data;

public class MapDBMapper {

    private MapDBMapper() { }

    public static Data mapToData(MapDBData mapDBData) {
        return new Data(mapDBData.getId(), mapDBData.getType(), mapDBData.getCreationDate(), mapDBData.getUpdateDate(), mapDBData.getCustomFields());
    }

    public static MapDBData mapToDBData(Data data) {
        return new MapDBData(data.getId(), data.getType(), data.getCustomFields(), data.getCreationDate(), data.getUpdateDate());
    }
}
