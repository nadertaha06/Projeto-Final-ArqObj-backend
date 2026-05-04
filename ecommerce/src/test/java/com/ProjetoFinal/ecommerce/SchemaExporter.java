package com.ProjetoFinal.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Generates V1__schema_inicial.sql from current @Entity classes.
 * Run with:
 *   ./mvnw -Dtest=SchemaExporter -DfailIfNoTests=false test
 *
 * Boots Spring with an in-memory H2 just to satisfy the DataSource,
 * but forces MySQL dialect so the emitted DDL is valid MySQL.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:schemaexport;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false",
        "spring.jpa.properties.hibernate.format_sql=true",
        "spring.jpa.properties.hibernate.hbm2ddl.delimiter=;",
        "spring.jpa.properties.jakarta.persistence.schema-generation.scripts.action=create",
        "spring.jpa.properties.jakarta.persistence.schema-generation.scripts.create-target=src/main/resources/db/migration/V1__schema_inicial.sql",
        "spring.flyway.enabled=false",
        "jwt.secret=schema-export-only-not-used-anywhere-just-a-placeholder-value-32b"
})
class SchemaExporter {

    @Test
    void exportSchema() {
    }
}