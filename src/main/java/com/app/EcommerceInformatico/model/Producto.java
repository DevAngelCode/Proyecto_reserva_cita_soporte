package com.app.EcommerceInformatico.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 500)
	private String nombre;
	@Column(length = 5000)
	private String descripcion;
	private String categoria;
	private double precio;
	private int stock;
	private String imagen;
	private int descuento;
	private double precioDescuento;
	private boolean estado;

}
