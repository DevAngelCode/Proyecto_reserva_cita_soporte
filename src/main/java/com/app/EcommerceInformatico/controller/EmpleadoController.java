package com.app.EcommerceInformatico.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
@RequestMapping("/empleado")
public class EmpleadoController {
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

	

	@GetMapping("/")
	public String index() {
		return "empleado/home";
	}
	@GetMapping("/citas")
	public String citas(Principal p, Model model) {
		User userDtls = getUserDetails(p);
		List<Cita> citas = citaService.obtenerCitasPorEmpleadoAndEstado(userDtls.getId(), true );
		model.addAttribute("citas", citas);
		
		
		
		return "empleado/citas";
	}
	@GetMapping("/citasPasadas")
	public String citasPasadas(Principal p, Model model) {
		User userDtls = getUserDetails(p);
		List<Cita> citas = citaService.obtenerCitasPorEmpleadoAndEstado(userDtls.getId(), false);
		model.addAttribute("citas", citas);

		return "empleado/citasPasadas";
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
	// editar perfil
		@GetMapping("/editarPerfil")
		public String editarPerfil() {

			return "empleado/editarPerfil";
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

			return "redirect:/empleado/editarPerfil";
		}

		// cambiarContrasena
		@PostMapping("/cambiarContrasena")
		public String cambiarContrasena(@RequestParam Long userId, @RequestParam String password,
				@RequestParam String newPassword, @RequestParam String confirmNewPassword, HttpSession session) {

			User userU = userService.getUserById(userId);
			if (!passwordEncoder.matches(password, userU.getPassword())) {
				session.setAttribute("errorMsg", "La contraseña actual no coincide");
				return "redirect:/empleado/editarPerfil";
			}
			if (!newPassword.equals(confirmNewPassword)) {
				session.setAttribute("errorMsg", "La nueva contraseña y la confirmación de la contraseña no coinciden");
				return "redirect:/empleado/editarPerfil";
			}
			userU.setPassword(passwordEncoder.encode(newPassword));
			User updatedUser = userService.updateUser(userU);
			if (updatedUser != null) {
				session.setAttribute("succMsg", "Contraseña actualizada correctamente");
			} else {
				session.setAttribute("errorMsg", "Error al actualizar la contraseña");
			}

			return "redirect:/empleado/editarPerfil";

		}
	
}
