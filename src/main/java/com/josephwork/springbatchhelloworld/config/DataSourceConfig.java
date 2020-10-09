package com.josephwork.springbatchhelloworld.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean(name = "mssqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource appDataSource(){
        return DataSourceBuilder.create().build();
    }
    
	@Bean(name = "h2DataSource")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource.h2")
	public DataSource h2DataSource() {
	    return DataSourceBuilder.create()
//                .url("jdbc:h2:mem:thing:H2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
//                .driverClassName("org.h2.Driver")
//                .username("sa")
//                .password("")	 	    		
	    		.build();
	}
	
}
