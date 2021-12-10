package com.bolsadeideas.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "cotizacion")
public class Cotizacion implements Serializable {

	/*@Id
	@GenericGenerator(
			name = "SEQ_COTIZACION_AUTO_INCR",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "SEQ_COTIZACION_AUTO_INCR"),
					@Parameter(name = "initial_value", value = "1000"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@GeneratedValue(generator = "SEQ_COTIZACION_AUTO_INCR")*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//private String descripcion;

	private String observacion;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date createAt;

	@Column(name = "subtotal")
	private double subtotal;

	@Column(name = "igv")
	private double igv;

	@Column(name = "total")
	private double total;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Gestor gestor;

	/* CascadeType.ALL */
	/* Cada vez que se agrega una una cotizacion primero crea la cotizacion y luego agrega de cada linea del detalle cotizacion*/
	/* Cada vez que se elimina una una cotizacion primero cada linea del detalle cotizacion y luego la cotizacion*/
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cotizacion_id")
	private List<ItemCotizacion> items;

	public Cotizacion() {

		this.items = new ArrayList<ItemCotizacion>();
	}

	@PrePersist
	public void prePersist() {

		this.createAt = new Date();
		this.igv = this.cacularIGV();
	}

	private double cacularIGV() {

		return this.total * 0.18;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	/*public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}*/

	public String getObservacion() {

		return observacion;
	}

	public void setObservacion(String observacion) {

		this.observacion = observacion;
	}

	public Date getCreateAt() {

		return createAt;
	}

	public void setCreateAt(Date createAt) {

		this.createAt = createAt;
	}

	public Double getSubtotal() {

		return Math.round(subtotal*100.0)/100.0;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getIgv() {

		return Math.round(igv*100.0)/100.0;
	}

	public void setIgv(double igv) {
		this.igv = igv;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@XmlTransient // Omite este atributo en la serializacion, no lo incluye en el XML
	public Gestor getGestor() {

		return gestor;
	}

	public void setGestor(Gestor gestor) {

		this.gestor = gestor;
	}

	public List<ItemCotizacion> getItems() {

		return items;
	}

	public void setItems(List<ItemCotizacion> items) {
		
		this.items = items;
	}

	public void addItemCotizacion(ItemCotizacion item) {

		this.items.add(item);
	}

	public Double getTotal() {
		Double total = 0.0;

		int size = items.size();

		for (int i = 0; i < size; i++) {
			total += items.get(i).calcularImporte();
		}

		return Math.round(total*100.0)/100.0;

	}

	public void calculaIGV(){
		Double igv = 0.0;
		igv = this.getTotal() * 0.18;
		setIgv(Math.round(igv*100.0)/100.0);

	}
	public void calculaSubTotal(){
		Double subTotal = 0.0;
		subTotal = this.getTotal() * 0.82;
		setSubtotal(Math.round(subTotal*100.0)/100.0);
	}

	public void calculaTotal(){
		Double total = 0.0;
		total = this.getTotal();
		setTotal(Math.round(total*100.0)/100.0);
	}

	private static final long serialVersionUID = 1L;
}