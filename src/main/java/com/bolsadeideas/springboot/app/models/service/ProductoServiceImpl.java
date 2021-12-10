package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Repository("productoServiceJPA")
public class ProductoServiceImpl implements IProductoService{

    @Autowired
    private IProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll(String keyword) {

        if(keyword != null){
            return productoDao.findAll(keyword);
        }
        return (List<Producto>)productoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> findAll(Pageable pageable) {

        return productoDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findOne(Long id) {

        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Producto producto) {

        productoDao.save(producto);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        productoDao.deleteById(id);
    }


}
