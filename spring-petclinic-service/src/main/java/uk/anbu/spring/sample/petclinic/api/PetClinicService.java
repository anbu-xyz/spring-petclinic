package uk.anbu.spring.sample.petclinic.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.anbu.spring.sample.petclinic.lib.PropertySourceBuilder;

import javax.sql.DataSource;

public class PetClinicService {
	private final AnnotationConfigApplicationContext petClinicContext;

	public PetClinicService(PetClinicContext.Config config) {
		this.petClinicContext = new AnnotationConfigApplicationContext();
		this.petClinicContext.addBeanFactoryPostProcessor(
			PropertySourceBuilder.propertyConfigurer(PetClinicContext.class));
		this.petClinicContext.registerBean(DataSource.class, config::dataSource);
		this.petClinicContext.register(PetClinicContext.class);

		this.petClinicContext.getEnvironment().getPropertySources()
			.addLast(PropertySourceBuilder.forService(PetClinicContext.class));

		this.petClinicContext.refresh();
	}

	public <T>T getBean(Class<T> clazz) {
		return petClinicContext.getBean(clazz);
	}
}
