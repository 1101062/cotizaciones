package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.view.xml.GestorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gestores")
public class GestorRestController {

    @Autowired
    private IGestorService gestorService;

    @GetMapping(value = "/listar")
    public GestorList listar() {

        return new GestorList(gestorService.findAll());
    }
}
