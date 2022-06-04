module conditions.iam {
    requires conditions.core;
    requires conditions.context;

    requires java.persistence;
    requires org.hibernate.orm.core;

    exports conditions.iam.model;
    exports conditions.iam.repository;
    exports conditions.iam.cross_border;
}