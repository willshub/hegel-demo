package org.hegel.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean

import javax.sql.DataSource

@SpringBootApplication
class HegelApplication {

    static void main(String[] args) {
        SpringApplication.run HegelApplication, args
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource hegelDataSource() {

        return DataSourceBuilder.create().build();
    }
}
