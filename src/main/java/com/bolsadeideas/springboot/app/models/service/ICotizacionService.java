package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICotizacionService {

    public List<Cotizacion> findAll();

    public Page<Cotizacion> findAll(Pageable pageable);

    public void save(Cotizacion cotizacion);

    public Cotizacion findOne(Long id);

    public void delete(Long id);

    //public Cotizacion fetchByIdWithProductos(Long id);
    public Cotizacion fetchByIdWithGestorWithItemCotizacionWithProducto(Long id);
}
