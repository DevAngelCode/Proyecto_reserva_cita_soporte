package com.app.EcommerceInformatico.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

	public List<Cita> findByIsEnabled(Boolean estado);

	public List<Cita> findByEmpleadoIdAndFecha(Long id, LocalDate fechaSeleccionada);

	public List<Cita> findByEmpleadoId(Long id);

	public List<Cita> findByIsEnabledAndUserId(boolean b, Long id);

	public List<Cita> findByEmpleadoIdAndIsEnabled(Long id, boolean b);
	

}
