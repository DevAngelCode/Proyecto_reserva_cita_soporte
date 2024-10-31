package com.app.EcommerceInformatico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public Boolean existsByNombre(String nombre);
	public List<Categoria> findByEstadoTrue();

}
