package com.example.JDBC_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Configuration
@ComponentScan("com.example.JDBC_project")
@EnableWebMvc
@PropertySource("classpath:database.properties")
public class SpringConfig {

    private final Environment environment;

    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Connection getConnection() {
        Connection connection;
        try {
            Class.forName(environment.getProperty("driver"));
            connection = DriverManager.getConnection(
                    Objects.requireNonNull(environment.getProperty("url")),
                    environment.getProperty("username_postgres"),
                    environment.getProperty("password")
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
