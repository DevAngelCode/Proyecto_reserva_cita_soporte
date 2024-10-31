package com.app.EcommerceInformatico.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.EcommerceInformatico.model.Categoria;
import com.app.EcommerceInformatico.repository.CategoriaRepository;
import com.app.EcommerceInformatico.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public Categoria saveCategoria(Categoria categoria) {
		// TODO Auto-generated method stub
		return categoriaRepository.save(categoria);
	}

	@Override
	public Boolean existCategoria(String nombre) {
		// TODO Auto-generated method stub
		return categoriaRepository.existsByNombre(nombre);
	}

	@Override
	public List<Categoria> getAllCategoria() {
		// TODO Auto-generated method stub
		return categoriaRepository.findAll();
	}

	@Override
	public Boolean deleteCategoria(Long id) {
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(categoria)) {
			categoriaRepository.deleteById(id);
			return true;
		}
		
		return false;
	}

	@Override
	public Categoria getCategoriaById(Long id) {
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		return categoria;
	}

	@Override
	public List<Categoria> getAllActiveCategoria() {
		List<Categoria> categorias = categoriaRepository.findByEstadoTrue();
		return categorias;
	}

}
