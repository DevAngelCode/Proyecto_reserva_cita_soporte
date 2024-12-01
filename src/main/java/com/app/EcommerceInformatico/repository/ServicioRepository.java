package com.app.EcommerceInformatico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    public Boolean existsByNombre(String nombre);
}
