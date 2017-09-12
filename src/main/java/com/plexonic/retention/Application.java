package com.plexonic.retention;

import liquibase.integration.spring.SpringLiquibase;
import lombok.Builder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {
	@Bean
	public ProjectionFactory projectionFactory() {
	    return new SpelAwareProxyProjectionFactory();
    }
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
