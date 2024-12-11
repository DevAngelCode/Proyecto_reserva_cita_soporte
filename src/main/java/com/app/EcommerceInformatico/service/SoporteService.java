package com.app.EcommerceInformatico.service;

import java.util.List;

import com.app.EcommerceInformatico.model.Soporte;

public interface SoporteService {
	public List<Soporte> getAllSoporte();
	public Soporte getSoporteById(Long id);
	public Soporte saveSoporte(Soporte soporte);
	public void deleteSoporte(Long id);
	//obtener sopores por nombre
	public Soporte getSoporteByNombre(String nombre);
	

}
