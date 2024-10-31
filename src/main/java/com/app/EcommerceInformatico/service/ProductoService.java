package com.app.EcommerceInformatico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.EcommerceInformatico.model.Producto;

public interface ProductoService {
	public Producto saveProducto(Producto producto);
	public List<Producto> getAllProducto();
	public Producto getProductoById(Long id);
    public Boolean deleteProducto(Long id);
    public Producto updateProducto(Producto producto, MultipartFile imagen);
    public List<Producto> getAllActiveProducto();

}
