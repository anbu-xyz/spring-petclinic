/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.anbu.spring.sample.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.lib.ResettableGlobalUtcClock;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 * when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenFirstNameEmpty() {
		var clock = new ResettableGlobalUtcClock(
			LocalDate.of(2020, 3, 4),
			LocalTime.of(10, 10));

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        var owner = new OwnerEntity();
		owner.setFirstName("");
		owner.setLastName("smith");
		owner.setCity("Angel City");
		owner.setAddress("20, Honey Blvd");
		owner.setTelephone("1234567890");
		owner.setUpdateTimestampUtc(clock.sqlTimestamp());

        Validator validator = createValidator();
        Set<ConstraintViolation<OwnerEntity>> constraintViolations = validator.validate(owner);

        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<OwnerEntity> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("firstName");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");
    }

}
