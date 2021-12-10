package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {

    public List<User> findAll();

    public Page<User> findAll(Pageable pageable);

    public void save(User gestor);

    public User findOne(Long id);


    public void delete(Long id);

}
