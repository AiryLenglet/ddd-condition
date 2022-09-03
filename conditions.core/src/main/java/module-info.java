module conditions.core {
    requires conditions.common;

    requires java.transaction;
    requires java.persistence;
    requires jakarta.inject;
    requires org.hibernate.orm.core;

    requires org.slf4j;

    exports conditions.core.repository;
    exports conditions.core.repository.listener;
    exports conditions.core.model;
    opens conditions.core.model;
    exports conditions.core.model.task;
    exports conditions.core.use_case;
    exports conditions.core.event_handler;
    exports conditions.core.service;

    exports conditions.core.factory;

    exports conditions.core.event;
    exports conditions.core.event.condition; // ?
    exports conditions.core.event.fulfillment;
    exports conditions.core.event.approval;
    opens conditions.core.model.task;
}