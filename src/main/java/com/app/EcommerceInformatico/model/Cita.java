package com.app.EcommerceInformatico.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cita {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false)
	private User empleado;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	private LocalDate fecha;
	private LocalTime hora;
	private String estado;
	private Boolean isEnabled;

	
	
}
