package com.example.vavaplanit;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

public class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static PostgresqlContainer container;

    private PostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresqlContainer getInstance() {
        if (container == null) {
            container = new PostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(container, ""),"classpath:schema.sql");
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
