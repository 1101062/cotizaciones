
package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/* Apache tomcat

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
 */

/* Jboss o Wildfly */
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "gestor")
public class Gestor implements Serializable {

	/*
    @Id
    @GenericGenerator(
            name = "SEQ_CLIENTE_AUTO_INCR",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "SEQ_CLIENTE_AUTO_INCR"),
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
	@Column( name = "nombres")
	private String nombres;

	@NotEmpty
	@Column( name = "apellidos")
	private String apellidos;

	@NotEmpty
	@Column( name = "dni", unique = true)
	private String dni;

	@NotEmpty
	@Column( name = "telefono")
	private String telefono;

	@NotEmpty
	@Email
	@Column( name = "email", unique = true)
	private String email;

	@NotEmpty
	@Column( name = "distrito")
	private String distrito;

	@OneToMany(mappedBy = "gestor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Cotizacion> cotizaciones;

	public Gestor() {
		cotizaciones = new ArrayList<Cotizacion>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public List<Cotizacion> getCotizaciones() {
		return cotizaciones;
	}

	public void setCotizaciones(List<Cotizacion> cotizaciones) {
		this.cotizaciones = cotizaciones;
	}

	public void addCotizacion(Cotizacion cotizacion) {
		cotizaciones.add(cotizacion);
	}



	private static final long serialVersionUID = 1L;


}
