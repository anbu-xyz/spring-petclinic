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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.anbu.spring.sample.petclinic.service.db.entity.VetEntity;
import uk.anbu.spring.sample.petclinic.ui.system.controller.VetController;
import uk.anbu.spring.sample.petclinic.service.db.repository.VetRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link VetController}
 */

@WebMvcTest(VetController.class)
@DisabledInNativeImage
@DisabledInAotMode
@Disabled
class VetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetRepository vets;

    private VetEntity james() {
        VetEntity james = new VetEntity();
        james.setFirstName("James");
        james.setLastName("Carter");
        james.setEid(1);
        return james;
    }

    private VetEntity helen() {
        VetEntity helen = new VetEntity();
        helen.setFirstName("Helen");
        helen.setLastName("Leary");
        helen.setEid(2);
        helen.addSpecialty("radiology");
        return helen;
    }

    @BeforeEach
    void setup() {
        given(this.vets.findAll()).willReturn(Lists.newArrayList(james(), helen()));
        given(this.vets.findAll(any(Pageable.class)))
            .willReturn(new PageImpl<VetEntity>(Lists.newArrayList(james(), helen())));

    }

    @Test
    void testShowVetListHtml() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/vets.html?page=1"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("listVets"))
            .andExpect(view().name("vets/vetList"));

    }

    @Test
    void testShowResourcesVetList() throws Exception {
        ResultActions actions = mockMvc.perform(get("/vets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        actions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.vetList[0].id").value(1));
    }

}
