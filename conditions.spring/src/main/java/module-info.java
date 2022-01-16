open module conditions.spring {
    requires conditions.core;
    requires conditions.context;
    requires conditions.iam;
    requires conditions.bat;

    requires org.slf4j;

    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.beans;
    requires spring.web;
    requires spring.tx;

    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.data.envers;
    requires java.persistence;

}