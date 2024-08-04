package uk.anbu.spring.sample.petclinic.service.internal.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vets {

    private List<VetEntity> vets;

    @XmlElement
    public List<VetEntity> getVetList() {
        if (vets == null) {
            vets = new ArrayList<>();
        }
        return vets;
    }

}
