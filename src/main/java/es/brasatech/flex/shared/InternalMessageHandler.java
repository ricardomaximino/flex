package es.brasatech.flex.shared;

public interface InternalMessageHandler {

    void handle(InternalDataEvent<Flex> internalDataEvent);
}
