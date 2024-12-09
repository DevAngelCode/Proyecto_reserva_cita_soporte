package com.app.EcommerceInformatico.model;

import java.util.Date;

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
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String celular;
	private String email;
	private String direccion;
	private String ciudad;
	private String codigoPostal;
	private String password;
	private String imagenPerfil;
	private String rol;
	private Boolean isEnable;
	private Boolean cuentaNoBloqueada;
	private Integer intentosFallidos;
	private Date tiempoBloqueo;
	private String resetToken;

}
