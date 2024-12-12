package com.app.EcommerceInformatico.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.EcommerceInformatico.model.Cita;
import com.app.EcommerceInformatico.repository.CitaRepository;
import com.app.EcommerceInformatico.service.CitaService;

@Service
public class CitaServiceImpl implements CitaService {
	@Autowired
	private CitaRepository citaRepository;

	@Override
	public Cita saveCita(Cita cita) {
		Cita savedCita = citaRepository.save(cita);
		return savedCita;
	}

	@Override
	public Cita getCitaById(Long id) {
		Cita cita = citaRepository.findById(id).get();
		return cita;
	}

	@Override
	public List<Cita> getAllCitas() {
		List<Cita> citas = citaRepository.findAll();
		return citas;
	}

	@Override
	public List<Cita> getCitasByEstado(Boolean isEnabled) {
		List<Cita> citas = citaRepository.findByIsEnabled(isEnabled);
		return citas;
	}

	@Override
	public void deleteCita(Long id) {
		citaRepository.deleteById(id);
		
	}

	@Override
	public List<Cita> obtenerCitasPorEmpleadoYFecha(Long id, LocalDate fechaSeleccionada) {
		List<Cita> citas = citaRepository.findByEmpleadoIdAndFecha(id, fechaSeleccionada);
		return citas;
	}

	
}