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

package uk.anbu.spring.sample.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.ui.system.controller.PetTypeFormatter;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
class PetTypeFormatterTests {

    @Mock
    private OwnerRepository pets;

    private PetTypeFormatter petTypeFormatter;

    @BeforeEach
    void setup() {
        this.petTypeFormatter = new PetTypeFormatter(pets);
    }

    @Test
    void testPrint() {
        Pet.PetType petType = Pet.PetType.of("Hamster");
        String petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH);
        assertThat(petTypeName).isEqualTo("HAMSTER");
    }

    @Test
    void shouldParse() throws ParseException {
        Pet.PetType petType = petTypeFormatter.parse("BIRD", Locale.ENGLISH);
        assertThat(petType.code()).isEqualTo("BIRD");
    }

    @Test
    void shouldThrowParseException() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> {
            petTypeFormatter.parse("Fish", Locale.ENGLISH);
        });
    }

}
