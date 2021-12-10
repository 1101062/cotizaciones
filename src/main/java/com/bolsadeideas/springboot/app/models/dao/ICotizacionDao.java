package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICotizacionDao extends PagingAndSortingRepository<Cotizacion, Long> {

    @Query("select f from Cotizacion f join fetch f.gestor c join fetch f.items l join fetch l.producto where f.id=?1")
    public Cotizacion fetchByIdWithGestorWithItemCotizacionWithProducto(Long id);

}