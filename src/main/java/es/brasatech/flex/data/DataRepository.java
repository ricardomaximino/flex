package es.brasatech.flex.data;

import es.brasatech.flex.shared.Flex;

import java.time.LocalDateTime;
import java.util.List;


public interface DataRepository<T extends Flex> {

    List<T> findByType(String type);
    List<T> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);
}
