package com.app.EcommerceInformatico.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.EcommerceInformatico.model.Cita;
import com.app.EcommerceInformatico.repository.CitaRepository;
import com.app.EcommerceInformatico.service.CitaService;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public Cita guardarCita(Cita cita) {
        // TODO Auto-generated method stub
        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> obtenerCitas() {
        // TODO Auto-generated method stub
        return citaRepository.findAll();
    }

    @Override
    public Boolean eliminarCita(Long id) {
        // TODO Auto-generated method stub
        citaRepository.deleteById(id);
        return true;
    }
}
