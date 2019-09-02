package com.dokhabackend.dokha.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * Created by SemenovAE on 29.07.2019
 */

@Configuration
@EnableTransactionManagement
public class JpaConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.hikari.maximumPoolSize:5}")
    private String maximumPoolSize;

    @Bean
    public DataSource configureDataSource() {
//        LOGGER.debug("Configuring datasource {} {} {}", driverClassName, url, user);

        System.out.println("Hikari config : " + url + " " + user);
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPoolName("DokhaCP");
        config.setPassword(password);
        config.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
        config.setAutoCommit(true);
        config.setConnectionTimeout(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource hikariDataSource = new HikariDataSource(config);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);

        try {
            jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS DOKHA;");
        } catch (Exception e) {
//            log.error(e.getMessage());
        } finally {
            hikariDataSource.close();
        }

        config.setAutoCommit(false);
        return new HikariDataSource(config);
    }
}
