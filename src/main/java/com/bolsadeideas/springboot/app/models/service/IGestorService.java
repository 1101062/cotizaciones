package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IGestorService {

	public List<Gestor> findAll();

	public Page<Gestor> findAll(Pageable pageable);

	public List<Gestor> findAll(String keyword);

	public void save(Gestor gestor);

	public Gestor findOne(Long id);

	public Gestor fetchByIdWithCotizaciones(Long id);

	public void delete(Long id);

	public List<Producto> findByNombre(String term);
	
	public void saveCotizacion(Cotizacion cotizacion);
	
	public Producto findProductoById(Long id);

	public Cotizacion findCotizacionById(Long id);

	public void deleteCotizacion(Long id);

	public Cotizacion fetchCotizacionByIdWithGestorWithItemCotizacionWithProducto(Long id);

	public User findByUsername(String username);

}
