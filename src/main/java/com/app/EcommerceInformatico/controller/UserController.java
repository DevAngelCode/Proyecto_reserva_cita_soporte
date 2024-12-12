package com.app.EcommerceInformatico.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/user")
public class UserController {
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
	
	
	//reservar
	@GetMapping("/reservar")
	public String reservar(HttpSession session , Model model) {
		Long userId = (Long) session.getAttribute("empleadoId");
		String fecha = (String) session.getAttribute("fecha");
		String hora = (String) session.getAttribute("hora");
		if (userId == null || fecha == null || hora == null) {
			return "redirect:/paso3";
		}
		User empleado = userService.getUserById(userId);

		model.addAttribute("empleado", empleado);
		model.addAttribute("fecha", fecha);
		model.addAttribute("hora", hora);
		
		return "user/reservar";
	}
	
	//enviarCita
	@PostMapping("/enviarCita")
	public String enviarCita(HttpSession session,@RequestParam Long usuarioId, @RequestParam Long empleadoId, @RequestParam String fecha,
			@RequestParam String hora, Model model) {
		User usuario = userService.getUserById(usuarioId);
		User empleado = userService.getUserById(empleadoId);
		Cita cita = new Cita();
		cita.setUser(usuario);
		cita.setEmpleado(empleado);
		cita.setFecha(LocalDate.parse(fecha));
		cita.setHora(LocalTime.parse(hora));
		cita.setEstado("PENDIENTE");
		cita.setIsEnabled(true);
		Cita citaGuardada = citaService.saveCita(cita);
		if (citaGuardada != null) {
			
			session.removeAttribute("empleadoId");
			session.removeAttribute("fecha");
			session.removeAttribute("hora");
			session.setAttribute("succMsg", "Cita guardada correctamente");
			
			
		} else {
			session.setAttribute("errorMsg", "Error al guardar la cita");
			return "redirect:/user/reservar";

		}
		
	
		return "redirect:/";
	}

	//citas
	@GetMapping("/citas")
	public String citas(Model model) {
		List<Cita> citas = citaService.getCitasByEstado(true);
		model.addAttribute("citas", citas);
		
		return "user/citas";
	}
	//detalleCita
	@GetMapping("/cita/detalle/{id}")
	public String detalleCita(@PathVariable Long id, Model model) {
		Cita cita = citaService.getCitaById(id);
		model.addAttribute("cita", cita);
		return "user/detalle_cita";
	}
      
	

}
