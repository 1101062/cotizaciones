package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.ItemCotizacion;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IItemCotizacionDao extends PagingAndSortingRepository<ItemCotizacion, Long> {

}
