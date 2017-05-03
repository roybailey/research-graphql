package me.roybailey.springboot.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {"me.roybailey.springboot.domain"})
@EnableJpaRepositories(basePackages = {"me.roybailey.springboot.repository"})
public class JpaRepositoryConfiguration {
}
