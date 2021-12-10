package com.bolsadeideas.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
//import org.hibernate.validator.constraints.NotEmpty;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "producto")
public class Producto implements Serializable {

	/*
	@Id
	@GenericGenerator(
			name = "SEQ_PRODUCTO_AUTO_INCR",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "SEQ_PRODUCTO_AUTO_INCR"),
					@Parameter(name = "initial_value", value = "1000"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@GeneratedValue(generator = "SEQ_PRODUCTO_AUTO_INCR")*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Si no ingresamos el ID em el fichero import.sql usar IDENTITY
	private Long id;

	@NotEmpty
	@Size(max = 50)
	@Column(name = "nombre")
	private String nombre;

	@NotEmpty
	@Size(max = 15)
	@Column(name = "marca")
	private String marca;

	@NotEmpty
	@Size(max = 50)
	@Column(name = "modelo")
	private String modelo;

	@NotEmpty
	@Size(max = 15)
	@Column(name = "serie")
	private String serie;

	@NotEmpty
	@Size(max = 50)
	@Column(name = "fabricante")
	private String fabricante;

	@NotNull
	@Column(name = "stock")
	private int stock;

	@NotNull
	@Column(name = "precio")
	private Double precio;

	@Column(name = "descripcion")
	private String descripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Categoria categoria;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date createAt;

	@PrePersist
	public void prePersist() {

		createAt = new Date();
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

	public Double getPrecio() {

		return precio;
	}

	public void setPrecio(Double precio) {

		this.precio = precio;
	}

	public Date getCreateAt() {

		return createAt;
	}

	public void setCreateAt(Date createAt) {

		this.createAt = createAt;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	private static final long serialVersionUID = 1L;

}
