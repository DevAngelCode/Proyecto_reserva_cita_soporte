package com.app.EcommerceInformatico.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.EcommerceInformatico.model.Servicio;
import com.app.EcommerceInformatico.repository.ServicioRepository;
import com.app.EcommerceInformatico.service.ServicioService;

@Service
public class ServicioServiceImpl implements ServicioService {
    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public Servicio guardarServicio(Servicio servicio) {
        // TODO Auto-generated method stub
        return servicioRepository.save(servicio);
    }

    @Override
    public List<Servicio> obtenerServicios() {
        // TODO Auto-generated method stub
        return servicioRepository.findAll();
    }

    @Override
    public Boolean existeServicio(String nombre) {
        // TODO Auto-generated method stub
        return servicioRepository.existsByNombre(nombre);
    }

    @Override
    public void eliminarServicio(Long id) {
        // TODO Auto-generated method stub
        servicioRepository.deleteById(id);
    }

    @Override
    public Servicio obtenerServicio(Long id) {
        // TODO Auto-generated method stub
        Servicio servicio = servicioRepository.findById(id).orElse(null);
        return servicio;
    }
}
