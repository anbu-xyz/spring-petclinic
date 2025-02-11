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
package uk.anbu.spring.sample.petclinic.vet;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
class VetTests {

    @Test
    void testSerialization() {
        VetEntity vet = new VetEntity();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setEid(123);
        @SuppressWarnings("deprecation")
        VetEntity other = (VetEntity) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
        assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
        assertThat(other.getLastName()).isEqualTo(vet.getLastName());
        assertThat(other.getEid()).isEqualTo(vet.getEid());
    }

}
