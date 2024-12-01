package com.app.EcommerceInformatico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;
import com.app.EcommerceInformatico.model.Cita;
import com.app.EcommerceInformatico.service.CitaService;
import com.app.EcommerceInformatico.service.ServicioService;

@Controller
public class UserController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;
	@Autowired
	private CitaService citaService;
	@Autowired
	private ServicioService servicioService;

	@GetMapping
	public String home(Model model) {
		model.addAttribute("categorias", categoriaService.getAllActiveCategoria());
		model.addAttribute("productos", productoService.getAllActiveProducto());
		return "user/home";
	}

	@GetMapping("/login")
	public String mostrarFormularioAuth(Model model) {
		return "user/login";
	}

	@GetMapping("/solicitar")
	public String mostrarFormulario(Model model) {

		model.addAttribute("servicios", servicioService.obtenerServicios());

		model.addAttribute("cita", citaService.obtenerCitas());
		return "/user/solicitar";
	}

	@PostMapping("/procesarFormulario")
	public String procesarFormulario(@ModelAttribute Cita cita) {
		citaService.guardarCita(cita);
		return "redirect:/historial";
	}

	@GetMapping("/historial")
	public String historial(Model model) {
		model.addAttribute("citas", citaService.obtenerCitas());
		return "/user/historial";
	}

	@GetMapping("/historial/eliminar/{id}")
	public String eliminarCita(@PathVariable Long id) {

		citaService.eliminarCita(id);
		return "redirect:/historial";
	}

	@GetMapping("/nosotros")
	public String showNosotrosPage() {
		return "/user/nosotros";
	}

}
