package com.app.EcommerceInformatico.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.EcommerceInformatico.model.Cita;
import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.service.CitaService;
import com.app.EcommerceInformatico.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
	@Autowired
	private UserService userService;
	@Autowired
	private CitaService citaService;
	
	@ModelAttribute
	public void getUserDetails(Principal p, Model model) {
		if (p != null) {
			String email = p.getName();
			User userDtls = userService.getUserByEmail(email);
			model.addAttribute("user", userDtls);
			userDtls.setIntentosFallidos(0);

			// Integer countCart = cartService.getCounrCart(userDtls.getId());
			// model.addAttribute("countCart", countCart);

		}
		// List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		// model.addAttribute("categorys", allActiveCategory);
	}

	

	@GetMapping("/")
	public String index() {
		return "empleado/home";
	}
	@GetMapping("/citas")
	public String citas(Principal p, Model model) {
		User userDtls = getUserDetails(p);
		List<Cita> citas = citaService.obtenerCitasPorEmpleado(userDtls.getId());
		model.addAttribute("citas", citas);
		
		
		
		return "empleado/citas";
	}

	public User getUserDetails(Principal p) {
		if (p != null) {
			String email = p.getName();
			User userDtls = userService.getUserByEmail(email);
			return userDtls;
		}
		return null;
	}
	
	
	
	//cambiar de estado
	@GetMapping("/cambiarEstado/{id}")
	public String cambiarEstado(@PathVariable Long id,@RequestParam String estado,HttpSession session) {
		citaService.cambiarEstado(id,estado);
		session.setAttribute("succMsg", "Estado cambiado con exito");
		return "redirect:/empleado/citas";
	}
	@PostMapping("/guardarZoomLink")
	public String guardarZoomLink(@RequestParam Long citaId, @RequestParam String zoomLink, HttpSession session) {
		citaService.guardarZoomLink(citaId, zoomLink);
		session.setAttribute("succMsg", "Zoom link guardado con exito");
		return "redirect:/empleado/citas";
	}
}
