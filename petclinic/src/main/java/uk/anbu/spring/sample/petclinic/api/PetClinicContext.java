package uk.anbu.spring.sample.petclinic.api;

import jakarta.persistence.EntityManagerFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.anbu.spring.sample.petclinic.api.db.liquibase.LiquibaseDatabaseInitializer;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@ComponentScan
@Configuration
@EnableJpaRepositories("uk.anbu.spring.sample.petclinic.api.*")
@EnableTransactionManagement
public class PetClinicContext {

	@Bean
	LiquibaseDatabaseInitializer liquibaseDatabaseInitializer(DataSource dataSource) {
		return new LiquibaseDatabaseInitializer(dataSource);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		vendorAdapter.setGenerateDdl(false);
		vendorAdapter.setShowSql(false);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setDataSource(dataSource);

		Properties props = new Properties();
		// TODO: Do these work?
		props.put("hibernate.generate_statistics", "true");
		props.put("hibernate.session.events.log.LOG_QUEIRS_SLOWER_THAN_MS", "10");
		props.put("hibernate.jmx.enabled", "true");
		props.put("hibernate.jmx.usePlatformServer", "true");

		factory.setJpaProperties(props);
		factory.setPackagesToScan("uk.anbu.spring.sample.petclinic.api");

		return factory;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(AbstractEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getNativeEntityManagerFactory();
	}

	@Builder
	public record Config(DataSource dataSource, GlobalUtcClock clock) {
	}
}
