package es.brasatech.flex.mongodb;

import es.brasatech.flex.data.Data;

public class MongoDBMapper {

    private MongoDBMapper(){}

    public static Data mapToData(MongoDBData mongoData) {
        return new Data(mongoData.getId(), mongoData.getType(), mongoData.getCreationDate(), mongoData.getUpdateDate(), mongoData.getCustomFields());
    }

    public static MongoDBData mapToMongoDBData(Data data) {
        return new MongoDBData(data.getId(), data.getType(), data.getCustomFields(), data.getCreationDate(), data.getUpdateDate());
    }
}
