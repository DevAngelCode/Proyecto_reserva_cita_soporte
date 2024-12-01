package com.app.EcommerceInformatico.service;

import java.util.List;

import com.app.EcommerceInformatico.model.Servicio;

public interface ServicioService {
    public Servicio guardarServicio(Servicio servicio);

    public List<Servicio> obtenerServicios();

    public Boolean existeServicio(String nombre);

    public void eliminarServicio(Long id);

    public Servicio obtenerServicio(Long id);

}
