package com.app.EcommerceInformatico.service;

import java.util.List;

import com.app.EcommerceInformatico.model.Categoria;

public interface CategoriaService {
	public Categoria saveCategoria(Categoria categoria);
	public Boolean existCategoria(String nombre);
	public List<Categoria> getAllCategoria();
	public void deleteCategoria(Long id);
	public Categoria getCategoriaById(Long id);
	public List<Categoria> getAllActiveCategoria();

}
