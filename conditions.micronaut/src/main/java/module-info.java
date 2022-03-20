open module conditions.spring {
    requires conditions.core;
    requires conditions.context;
    requires conditions.iam;
    requires conditions.bat;

    requires org.slf4j;

    requires jakarta.inject;

    requires java.persistence;
    requires java.transaction; //to be removed

    requires io.micronaut.context;
    requires io.micronaut.inject;
    requires io.micronaut.http;
    requires io.micronaut.core;
    requires io.micronaut.data.data_model;
    requires io.micronaut.runtime;
    requires io.micronaut.data.data_tx;

    requires com.fasterxml.jackson.annotation;
}