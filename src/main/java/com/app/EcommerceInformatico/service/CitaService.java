package com.app.EcommerceInformatico.service;

import java.util.List;

import com.app.EcommerceInformatico.model.Cita;

public interface CitaService {
    public Cita guardarCita(Cita cita);

    public List<Cita> obtenerCitas();

    public Boolean eliminarCita(Long id);

}
