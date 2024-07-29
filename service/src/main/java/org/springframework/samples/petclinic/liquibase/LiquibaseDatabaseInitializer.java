package org.springframework.samples.petclinic.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiquibaseDatabaseInitializer implements InitializingBean {
	private final DataSource dataSource;

	@Override
	public void afterPropertiesSet() throws Exception {
		updateDatabase();
	}

	@SneakyThrows
	private void updateDatabase() {
		try (var conn = dataSource.getConnection()) {
			var dbConnection = new JdbcConnection(conn);
			Database database = DatabaseFactory.getInstance()
				.findCorrectDatabaseImplementation(dbConnection);

			var liquibaseFileName = "/db/liquibase-changelog.yaml";
			try (var stream = this.getClass().getResourceAsStream(liquibaseFileName)) {
				if (stream == null) {
					log.info("liquibase changelog {} is missing", liquibaseFileName);
					throw new IOException(String.format("Liquibase changelog %s is missing", liquibaseFileName));
				} else {
					log.info("Found changelog file {}", liquibaseFileName);
				}
			}

			var liquibase = new Liquibase(liquibaseFileName,
				new ClassLoaderResourceAccessor(),
				database);

			liquibase.update("");
		}
	}
}
