package com.app.EcommerceInformatico.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	private PasswordEncoder passwordEncoder;

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

	// reservar
	@GetMapping("/reservar")
	public String reservar(HttpSession session, Model model) {
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

	// enviarCita
	@PostMapping("/enviarCita")
	public String enviarCita(HttpSession session, @RequestParam Long usuarioId, @RequestParam Long empleadoId,
			@RequestParam String fecha, @RequestParam String hora, Model model) {
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

	// citas
	@GetMapping("/citas")
	public String citas(Principal p, Model model) {
		User userDtls = userService.getUserByEmail(p.getName());

		// obtener citas mias y q sea true
		List<Cita> citas = citaService.getCitasByEstadoAndUserId(true, userDtls.getId());
		model.addAttribute("citas", citas);

		return "user/citas";
	}

	// detalleCita
	@GetMapping("/cita/detalle/{id}")
	public String detalleCita(@PathVariable Long id, Model model) {
		Cita cita = citaService.getCitaById(id);
		model.addAttribute("cita", cita);
		return "user/detalle_cita";
	}

	// eliminarCita
	@GetMapping("/cita/eliminar/{id}")
	public String eliminarCita(@PathVariable Long id, Model model, HttpSession session) {
		try {
			citaService.deleteCita(id);
			session.setAttribute("succMsg", "Cita eliminada correctamente");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error al eliminar la cita");
		}

		return "redirect:/user/citas";
	}

	// citaspasadas
	@GetMapping("/citasPasadas")
	public String citasPasadas(Principal p, Model model) {

		User userDtls = userService.getUserByEmail(p.getName());
		List<Cita> citas = citaService.getCitasByEstadoAndUserId(false, userDtls.getId());
		model.addAttribute("citas", citas);

		return "user/citasPasadas";
	}

	@GetMapping("/editarPerfil")
	public String editarPerfil() {

		return "/user/editarPerfil";
	}

	@PostMapping("/update_perfil")
	public String updatePerfil(@ModelAttribute User user, MultipartFile file, HttpSession session) throws IOException {
		User userDtls = userService.getUserById(user.getId());
		String imageName = file.isEmpty() ? userDtls.getImagenPerfil() : file.getOriginalFilename();
		user.setImagenPerfil(imageName);
		User updatedUser = userService.updateUser(user);
		if (updatedUser != null) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "usuario_img" + File.separator
						+ file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Perfil actualizado correctamente");
		} else {
			session.setAttribute("errorMsg", "Error al actualizar el perfil");
		}

		return "redirect:/user/editarPerfil";
	}

	// cambiarContrasena
	@PostMapping("/cambiarContrasena")
	public String cambiarContrasena(@RequestParam Long userId, @RequestParam String password,
			@RequestParam String newPassword, @RequestParam String confirmNewPassword, HttpSession session) {

		User userU = userService.getUserById(userId);
		if (!passwordEncoder.matches(password, userU.getPassword())) {
			session.setAttribute("errorMsg", "La contraseña actual no coincide");
			return "redirect:/user/editarPerfil";
		}
		if (!newPassword.equals(confirmNewPassword)) {
			session.setAttribute("errorMsg", "La nueva contraseña y la confirmación de la contraseña no coinciden");
			return "redirect:/user/editarPerfil";
		}
		userU.setPassword(passwordEncoder.encode(newPassword));
		User updatedUser = userService.updateUser(userU);
		if (updatedUser != null) {
			session.setAttribute("succMsg", "Contraseña actualizada correctamente");
		} else {
			session.setAttribute("errorMsg", "Error al actualizar la contraseña");
		}

		return "redirect:/user/editarPerfil";

	}

}
