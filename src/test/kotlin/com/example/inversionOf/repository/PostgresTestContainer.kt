package com.example.inversionOf.repository

import org.flywaydb.core.Flyway
import org.junit.ClassRule
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.testcontainers.containers.PostgreSQLContainer

// shared between all test classes
abstract class PostgresTestContainer {

    companion object {
        @ClassRule
        val postgresSqlContainer = PostgreSQLContainer<Nothing>("postgres:13.1-alpine")

        val namedParameterJdbcTemplate: NamedParameterJdbcTemplate

        init {
            postgresSqlContainer.start()
            val dataSource = DriverManagerDataSource()
            dataSource.setDriverClassName(postgresSqlContainer.driverClassName)
            dataSource.url = postgresSqlContainer.jdbcUrl
            dataSource.username = postgresSqlContainer.username
            dataSource.password = postgresSqlContainer.password
            Flyway.configure()
                .dataSource(dataSource)
                .load()
                .migrate()
            namedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource)
        }
    }
}
