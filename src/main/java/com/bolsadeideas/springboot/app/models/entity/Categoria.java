package com.bolsadeideas.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/* Apache tomcat
import javax.validation.constraints.NotEmpty;
*/
/* Jboss o Wildfly */
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    /*
    @Id
    @GenericGenerator(
            name = "SEQ_CARACTERISTICA_AUTO_INCR",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "SEQ_CARACTERISTICA_AUTO_INCR"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "SEQ_CLIENTE_AUTO_INCR")

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Si no ingresamos el ID em el fichero import.sql usar IDENTITY
    private Long id;

    @NotEmpty
    @Size(min = 4, max = 50)
    @Column( name = "nombre")
    private String nombre;

    // carga perezosa, cada ves que se haga un getproductos(), recien traerá las productos de esa categoria
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Producto> productos;

    public Categoria() {
        productos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto){
        this.productos.add(producto);
    }

    private static final long serialVersionUID = 1L;
}
