package com.youpassed.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.youpassed.service"})
@Slf4j
public class GeneralConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfig.class);


/*	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		return dataSource;
	}*/


/*	@Bean
	public DataSource dataSource () {
		try {
			EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
			return dbBuilder.setType(EmbeddedDatabaseType.H2)
					.addScripts("classpath:dЬ/schema.sql", "classpath:dЬ/data.sql")
					.build();
		} catch (Exception е) {
			LOGGER.error("Embedded DataSource bean cannot Ье created!", е);
			return null;
		}
	}*/




}