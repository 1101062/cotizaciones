package com.bolsadeideas.springboot.app.view.xml;

import com.bolsadeideas.springboot.app.models.entity.Gestor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "gestorList")
public class GestorList {

    @XmlElement(name = "gestor")
    public List<Gestor> gestores;

    public GestorList() {
    }

    public GestorList(List<Gestor> gestores) {
        this.gestores = gestores;
    }

    public List<Gestor> getGestores() {
        return gestores;
    }
}
