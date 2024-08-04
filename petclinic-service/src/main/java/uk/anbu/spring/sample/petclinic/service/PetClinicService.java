package uk.anbu.spring.sample.petclinic.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.lib.PropertySourceBuilder;

import javax.sql.DataSource;

public class PetClinicService {
	private final AnnotationConfigApplicationContext petClinicContext;

	public PetClinicService(PetClinicServiceContext.Config config) {
		this.petClinicContext = new AnnotationConfigApplicationContext();
		this.petClinicContext.addBeanFactoryPostProcessor(
			PropertySourceBuilder.propertyConfigurer(PetClinicServiceContext.class));
		this.petClinicContext.registerBean(DataSource.class, config::dataSource);
		this.petClinicContext.registerBean(GlobalUtcClock.class, config::clock);
		this.petClinicContext.register(PetClinicServiceContext.class);

		this.petClinicContext.getEnvironment().getPropertySources()
			.addLast(PropertySourceBuilder.forService(PetClinicServiceContext.class));

		this.petClinicContext.refresh();
	}

	public <T>T getBean(Class<T> clazz) {
		return petClinicContext.getBean(clazz);
	}
}
