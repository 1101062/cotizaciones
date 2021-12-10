package com.bolsadeideas.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "item_cotizacion")
public class ItemCotizacion implements Serializable {

	/*@Id
	@GenericGenerator(
			name = "SEQ_ITEM_COTIZACION_AUTO_INCR",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "SEQ_ITEM_COTIZACION_AUTO_INCR"),
					@Parameter(name = "initial_value", value = "1000"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@GeneratedValue(generator = "SEQ_ITEM_COTIZACION_AUTO_INCR")*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "unidad_med")
	private String unidad_med;

	@Column(name = "cantidad")
	private Integer cantidad;

	@Column(name = "precio_unit")
	private double precio_unit;

	@Column(name = "subtotal")
	private double subtotal;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Producto producto;

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUnidad_med() {
		return unidad_med;
	}

	public void setUnidad_med(String unidad_med) {
		this.unidad_med = unidad_med;
	}

	public double getPrecio_unit() {
		return precio_unit;
	}

	public void setPrecio_unit(double precio_unit) {
		this.precio_unit = precio_unit;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getCantidad() {

		return cantidad;
	}

	public void setCantidad(Integer cantidad) {

		this.cantidad = cantidad;
	}

	public Double calcularImporte()
	{

		return Math.round(cantidad.doubleValue() * producto.getPrecio() * 100.00)/100.00;
	}

	public Producto getProducto() {

		return producto;
	}

	public void setProducto(Producto producto) {

		this.producto = producto;
	}
	
	private static final long serialVersionUID = 1L;

}