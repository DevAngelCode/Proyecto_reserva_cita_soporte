package com.app.EcommerceInformatico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;

@Controller
public class UserController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;

	@GetMapping
	public String home(Model model) {
		model.addAttribute("categorias", categoriaService.getAllActiveCategoria());
		model.addAttribute("productos", productoService.getAllActiveProducto());
		return "user/home";
	}
}
