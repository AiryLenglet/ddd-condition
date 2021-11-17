open module conditions.spring {
    requires conditions.core;
    requires conditions.iam;

    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.beans;
    requires spring.web;
    requires spring.tx;

    requires lombok;
    requires spring.data.jpa;

}