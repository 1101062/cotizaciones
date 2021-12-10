package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.ICategoriaDao;
import com.bolsadeideas.springboot.app.models.entity.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Repository("categoriaServiceJPA")
public class CategoriaServiceImpl implements ICategoriaService{

    @Autowired
    private ICategoriaDao categoriaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {

        return (List<Categoria>) categoriaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria findOne(Long id) {

        return categoriaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> findAll(Pageable pageable) {

        return categoriaDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Categoria categoria) {

        categoriaDao.save(categoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        categoriaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria fetchByIdWithProductos(Long id) {

        return categoriaDao.fetchByIdWithProductos(id);
    }
}

