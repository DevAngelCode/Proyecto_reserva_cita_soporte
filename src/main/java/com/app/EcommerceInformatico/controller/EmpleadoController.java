package com.app.EcommerceInformatico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

	@GetMapping("/")
	public String index() {
		return "empleado/home";
	}
}
