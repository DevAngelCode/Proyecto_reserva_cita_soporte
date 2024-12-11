package com.app.EcommerceInformatico.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.EcommerceInformatico.model.Soporte;
import com.app.EcommerceInformatico.repository.SoporteRepository;
import com.app.EcommerceInformatico.service.SoporteService;

@Service
public class SoporteServiceImpl implements SoporteService {
	@Autowired
	private SoporteRepository soporteRepository;

	@Override
	public List<Soporte> getAllSoporte() {
		List<Soporte> soportes = soporteRepository.findAll();
		return soportes;
	}

	@Override
	public Soporte getSoporteById(Long id) {
		Soporte soporte = soporteRepository.findById(id).get();
		return soporte;
	}

	@Override
	public Soporte saveSoporte(Soporte soporte) {
		Soporte saveSoporte = soporteRepository.save(soporte);
		return saveSoporte;
	}

	@Override
	public void deleteSoporte(Long id) {
		soporteRepository.deleteById(id);

	}

	@Override
	public Soporte getSoporteByNombre(String nombre) {
		Soporte soporte = soporteRepository.findByNombre(nombre);
		return soporte;
	}

}
