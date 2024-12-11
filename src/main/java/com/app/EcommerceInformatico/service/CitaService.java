package com.app.EcommerceInformatico.service;

import java.time.LocalDate;
import java.util.List;

import com.app.EcommerceInformatico.model.Cita;

public interface CitaService {
	public Cita saveCita(Cita cita);
	public Cita getCitaById(Long id);
	public List<Cita> getAllCitas();
	//obtener citas por estado
	public List<Cita> getCitasByEstado(Boolean isEnabled);
	public void deleteCita(Long id);
	public List<Cita> obtenerCitasPorEmpleadoYFecha(Long id, LocalDate fechaSeleccionada);
	
	

}
