package com.app.EcommerceInformatico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.Producto;
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	public List<Producto> findByEstadoTrue();

}
