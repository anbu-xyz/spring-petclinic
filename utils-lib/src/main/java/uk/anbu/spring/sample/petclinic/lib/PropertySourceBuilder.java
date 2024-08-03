package uk.anbu.spring.sample.petclinic.lib;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertySourceBuilder {
	@SneakyThrows
	public static <T> PropertySource<T> forService(Class<?> configClass) {
		Properties serviceProperties = loadProperties(configClass);
		return new PropertySource<>(String.format("%s.properties", mnemonic(configClass.getSimpleName()))) {
			public Object getProperty(String name) {
				return serviceProperties.getProperty(name);
			}
		};
	}

	public static PropertySourcesPlaceholderConfigurer propertyConfigurer(Class<?> configClass) {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setProperties(PropertySourceBuilder.loadProperties(configClass));

		return configurer;
	}

	@SneakyThrows
	private static Properties loadProperties(Class<?> configClass) {
		var properties = new Properties();
		var simpleClassName = configClass.getSimpleName();

		var propertiesFileName = "/" + mnemonic(simpleClassName) + ".properties";
		log.info("Reading properties from {} for context {}", propertiesFileName, simpleClassName);
		var stream = configClass.getResourceAsStream(propertiesFileName);
		if (stream == null) {
			throw new IOException("Missing properties file " + propertiesFileName);
		}
		properties.load(stream);
		if (!Objects.equals(properties.getProperty("app.mnemonic"), mnemonic(simpleClassName))) {
			log.warn("'app.mnemonic' was configured to {} in {}. It will be replaced with '{}'",
			   properties.getProperty("app.mnemonic"),
			   propertiesFileName,
			   mnemonic(simpleClassName));
		}
		properties.setProperty("app.mnemonic", mnemonic(simpleClassName));
		return properties;
	}

	private static String mnemonic(String simpleClassName) {
		return simpleClassName.replaceAll("Context$", "").toLowerCase(Locale.ENGLISH);
	}
}
