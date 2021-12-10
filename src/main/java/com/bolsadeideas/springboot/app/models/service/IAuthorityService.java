package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.entity.Authority;


import java.util.List;

public interface IAuthorityService {

    public List<Authority> findAll();
}
