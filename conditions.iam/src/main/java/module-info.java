module conditions.iam {
    requires conditions.core;
    requires conditions.context;

    requires java.persistence;

    exports conditions.iam.model;
    exports conditions.iam.repository;
    exports conditions.iam.cross_border;
}