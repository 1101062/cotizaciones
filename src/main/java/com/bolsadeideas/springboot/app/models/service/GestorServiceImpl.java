package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IGestorDao;
import com.bolsadeideas.springboot.app.models.dao.ICotizacionDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Service
@Repository("gestorServiceJPA")
public class GestorServiceImpl implements IGestorService {

	@Autowired
	private IGestorDao gestorDao;

	@Autowired
	private IProductoDao productoDao;

	@Autowired
	private ICotizacionDao cotizacionDao;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public List<Gestor> findAll() {
		// TODO Auto-generated method stub
		return (List<Gestor>) gestorDao.findAll();
	}

	@Override
	@Transactional
	public void save(Gestor gestor) {
		gestorDao.save(gestor);

	}

	@Override
	@Transactional(readOnly = true)
	public Gestor findOne(Long id) {
		// TODO Auto-generated method stub
		return gestorDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Gestor fetchByIdWithCotizaciones(Long id) {

		return gestorDao.fetchByIdWithCotizaciones(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {

		gestorDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<Gestor> findAll(Pageable pageable) {

		return gestorDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
//		return productoDao.findByNombre("%" + term + "%");
		return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveCotizacion(Cotizacion cotizacion) {

		cotizacionDao.save(cotizacion);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {

		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Cotizacion findCotizacionById(Long id) {

		return cotizacionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteCotizacion(Long id) {

		cotizacionDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cotizacion fetchCotizacionByIdWithGestorWithItemCotizacionWithProducto(Long id) {

		return cotizacionDao.fetchByIdWithGestorWithItemCotizacionWithProducto(id);
	}

	@Override
	public User findByUsername(String username) {

		return usuarioDao.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gestor> findAll(String keyword) {

		if(keyword != null){
			return  gestorDao.findAll(keyword);
		}
		return (List<Gestor>)gestorDao.findAll();
	}
}
