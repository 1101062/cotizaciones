package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {

    public List<Producto> findAll(String keyword);

    public Page<Producto> findAll(Pageable pageable);

    public void save(Producto producto);

    public Producto findOne(Long id);

    public void delete(Long id);


}
