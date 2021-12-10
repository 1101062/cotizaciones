package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Categoria;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoriaDao extends PagingAndSortingRepository<Categoria, Long> {

    @Query("select c from Categoria c left join fetch c.productos f where c.id=?1")
    public Categoria fetchByIdWithProductos(Long id);

//    @Query("select p from Producto  p where  p.categoria.id =?1 and p.nombre=?2")
//    public Producto productoByIdCategoria(Long id, String nombreProducto);
}
