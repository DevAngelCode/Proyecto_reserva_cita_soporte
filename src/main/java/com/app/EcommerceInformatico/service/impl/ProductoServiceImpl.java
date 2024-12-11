package com.app.EcommerceInformatico.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.app.EcommerceInformatico.model.Categoria;
import com.app.EcommerceInformatico.model.Producto;
import com.app.EcommerceInformatico.repository.ProductoRepository;
import com.app.EcommerceInformatico.service.ProductoService;
@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public Producto saveProducto(Producto producto) {

		return productoRepository.save(producto);
	}

	@Override
	public List<Producto> getAllProducto() {
		// TODO Auto-generated method stub
		return productoRepository.findAll();
	}

	@Override
	public Producto getProductoById(Long id) {

		Producto producto = productoRepository.findById(id).orElse(null);
		return producto;
	}

	@Override
	public Boolean deleteProducto(Long id) {

		Producto producto = productoRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(producto)) {
			productoRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public Producto updateProducto(Producto producto, MultipartFile imagen, Categoria categoria) {

		Producto dbProducto = getProductoById(producto.getId());

		String imagenNombre = imagen.isEmpty() ? dbProducto.getImagen() : imagen.getOriginalFilename();
		dbProducto.setNombre(producto.getNombre());
		dbProducto.setDescripcion(producto.getDescripcion());
		dbProducto.setCategoria(categoria);
		dbProducto.setPrecio(producto.getPrecio());
		dbProducto.setStock(producto.getStock());
		dbProducto.setEstado(producto.isEstado());
		dbProducto.setImagen(imagenNombre);
		dbProducto.setDescuento(producto.getDescuento());
		Double dsc = dbProducto.getPrecio() * (dbProducto.getDescuento() / 100.0);
		Double precioDescuento = dbProducto.getPrecio() - dsc;
		dbProducto.setPrecioDescuento(precioDescuento);
		
		Producto updateProducto = productoRepository.save(dbProducto);
		if (!ObjectUtils.isEmpty(updateProducto)) {
			
			if (!imagen.isEmpty()) {
				File saveFile;
				try {
					saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "producto_img" + File.separator
							+ imagen.getOriginalFilename());
					
                    Files.copy(imagen.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
				
			}
			
			return producto;
		}

		return null;
	}

	@Override
	public List<Producto> getAllActiveProducto() {
		List<Producto> productos = productoRepository.findByEstadoTrue();
		return productos;
	}

	@Override
	public Boolean existProducto(String nombre) {
		Boolean exist = productoRepository.existsByNombre(nombre);
		return exist;
	}

}
