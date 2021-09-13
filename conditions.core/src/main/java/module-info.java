module conditions.core {
    requires lombok;
    requires java.transaction;
    requires java.persistence;

    requires org.slf4j;

    exports conditions.core.repository;
    exports conditions.core.model;
    opens conditions.core.model;
    exports conditions.core.use_case;
    exports conditions.core.event_handler;

    exports conditions.core.factory;

    exports conditions.core.event;
    exports conditions.core.event.condition; // ?
    exports conditions.core.event.fulfillment;
    exports conditions.core.event.approval;

    exports conditions.core.model.draft;
    opens conditions.core.model.draft;

}