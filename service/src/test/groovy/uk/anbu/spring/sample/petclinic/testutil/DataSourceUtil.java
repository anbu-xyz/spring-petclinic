package uk.anbu.spring.sample.petclinic.testutil;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataSourceUtil {

	public static DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"); // In-memory H2 database
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		return dataSource;
	}}
