package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Gestor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IGestorDao extends PagingAndSortingRepository<Gestor, Long>{

    @Query("select c from Gestor c left join fetch c.cotizaciones z where c.id=?1")
    public Gestor fetchByIdWithCotizaciones(Long id);

    @Query(" select g from Gestor g where g.nombres like %?1% " +
            "or g.apellidos like %?1% " +
            "or g.dni like %?1%" +
            "or g.telefono like %?1%" +
            "or g.distrito like %?1%")
    public List<Gestor> findAll(String keyword);
}
