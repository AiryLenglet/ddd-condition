module conditions.bat {
    requires conditions.core;
    requires conditions.context;

    requires java.persistence;
    requires org.slf4j;

    exports conditions.business_audit_trail.repository;
}