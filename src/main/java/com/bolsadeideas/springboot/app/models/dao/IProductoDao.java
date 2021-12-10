package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);

	public List<Producto> findByNombreLikeIgnoreCase(String term);

	@Query("select c from Producto c where c.nombre like %?1%"
			+ "or c.marca like %?1% "
			+ "or c.categoria.nombre like %?1%"
			+ "or c.fabricante like %?1%"
			+ "or c.modelo like %?1%"
			+ "or c.serie like %?1%")
	public List<Producto> findAll(String keyword);
}