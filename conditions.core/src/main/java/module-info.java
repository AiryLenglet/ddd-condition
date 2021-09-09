module conditions.core {
    requires lombok;
    requires java.transaction;
    requires java.persistence;

    exports conditions.core.repository;
    exports conditions.core.model;
    exports conditions.core.use_case;
    exports conditions.core.event_handler;
    exports conditions.core.event;
    exports conditions.core.event.condition; // ?
    exports conditions.core.event.fulfillment;
}