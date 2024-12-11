package com.app.EcommerceInformatico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.Soporte;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {

	public Soporte findByNombre(String nombre);

}
