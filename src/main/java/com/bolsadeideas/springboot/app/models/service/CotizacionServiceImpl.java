package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.ICotizacionDao;
import com.bolsadeideas.springboot.app.models.entity.Categoria;
import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("cotizacionServiceJPA")
public class CotizacionServiceImpl implements ICotizacionService{

    @Autowired
    private ICotizacionDao cotizacionDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cotizacion> findAll() {
        return (List<Cotizacion>) cotizacionDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cotizacion> findAll(Pageable pageable) {
        return cotizacionDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Cotizacion cotizacion) {
        cotizacionDao.save(cotizacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Cotizacion findOne(Long id) {
        return  cotizacionDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cotizacionDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cotizacion fetchByIdWithGestorWithItemCotizacionWithProducto(Long id) {
        return cotizacionDao.fetchByIdWithGestorWithItemCotizacionWithProducto(id);
    }
}
