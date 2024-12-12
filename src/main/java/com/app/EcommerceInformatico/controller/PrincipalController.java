package com.app.EcommerceInformatico.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.EcommerceInformatico.model.Cita;
import com.app.EcommerceInformatico.model.Soporte;
import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.CitaService;
import com.app.EcommerceInformatico.service.ProductoService;
import com.app.EcommerceInformatico.service.SoporteService;
import com.app.EcommerceInformatico.service.UserService;
import com.app.EcommerceInformatico.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PrincipalController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;
	@Autowired
	private UserService userService;
	@Autowired
	private SoporteService soporteService;
	@Autowired
	private CommonUtil commonUtil;
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
	public String home(Model model) {
		model.addAttribute("categorias", categoriaService.getAllActiveCategoria());
		model.addAttribute("productos", productoService.getAllActiveProducto());
		return "home";
	}
	@GetMapping("/nosotros")
	public String nosotros() {
		return "sobreNosotros";
	}
	@GetMapping("/contacto")
	public String contacto() {
		return "contacto";
	}

	@GetMapping("/signin")
	public String inicioSesion() {
		return "signin";
	}

	// save user
	@PostMapping("/registrar")
	public String saveUser(@ModelAttribute User user, HttpSession session) throws IOException {
		User userByEmail = userService.getUserByEmail(user.getEmail());
		if (!ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "Usuario ya registrado");
			return "redirect:/signin";
		}
		
		
		User saveUser = userService.saveUser(user);
		if (!ObjectUtils.isEmpty(saveUser)) {

			session.setAttribute("succMsg", "Usuario registrado con éxito");

		} else {
			session.setAttribute("errorMsg", "Algo salió mal");

		}
		return "redirect:/signin";
	}

	/*
	 * //para token // recuperar_contraseña
	 * 
	 * @GetMapping("/recuperar") public String recuperar() { return
	 * "recuperar_contraseña"; }
	 * 
	 * @PostMapping("/recuperar") public String processForgotPassword(@RequestParam
	 * String email, HttpSession session, HttpServletRequest request) throws
	 * UnsupportedEncodingException, MessagingException { User userByEmail =
	 * userService.getUserByEmail(email); if (ObjectUtils.isEmpty(userByEmail)) {
	 * session.setAttribute("errorMsg", "Usuario no encontrado"); } else { String
	 * resetToken = UUID.randomUUID().toString();
	 * userService.updateUserResetToken(email, resetToken); // generate reset
	 * password link // http://localhost:8080/reset-password?token=dsaddsa String
	 * url = CommonUtil.generateUrl(request) + "/cambiar_contraseña?token=" +
	 * resetToken; // send email to user with token Boolean sendMail =
	 * commonUtil.sendMail(url, email); if (sendMail) {
	 * session.setAttribute("succMsg",
	 * "Enlace de restablecimiento de contraseña enviado a su correo"); } else {
	 * session.setAttribute("errorMsg", "Algo salió mal"); } } return
	 * "redirect:/recuperar";
	 * 
	 * }
	 * 
	 * // reset_password
	 * 
	 * @GetMapping("/cambiar_contraseña") public String resetPassword(@RequestParam
	 * String token, HttpSession session, Model m) {
	 * 
	 * User userByToken = userService.getUserByToken(token);
	 * 
	 * if (userByToken == null) { m.addAttribute("msg",
	 * "tu link es invalido o ha expirado !!"); return "mensaje"; }
	 * m.addAttribute("token", token); return "cambiar_contraseña"; }
	 */
	@GetMapping("/Recuperar_por_correo")
	public String recuperarPorCorreo() {
		return "recuperar";
	}

	@PostMapping("/Recuperar_por_correo")
	public String recuperarPorCorreo(@RequestParam String email, HttpSession session, Model model) {

		User userByEmail = userService.getUserByEmail(email);
		if (ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "Usuario no encontrado");
			return "redirect:/Recuperar_por_correo";

		} else {
			session.setAttribute("user", userByEmail);
			model.addAttribute("succMsg", "puedes cambiar tu contraseña");

		}
		return "redirect:/cambiarContrasena";

	}

	@GetMapping("/cambiarContrasena")
	public String cambiarContraseña(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/Recuperar_por_correo";
		}

		model.addAttribute("user", user);

		return "cambiar";
	}

	@PostMapping("/cambiarContrasena")
	public String cambiarContraseña(@RequestParam Long id, @RequestParam String password,
			@RequestParam String CPassword, HttpSession session) {
		User user = userService.getUserById(id);
		if (password.equals(CPassword)) {
			String encodePassword = passwordEncoder.encode(password);
			user.setPassword(encodePassword);
			userService.updateUser(user);
			session.setAttribute("succMsg", "Contraseña cambiada con éxito");
		} else {
			session.setAttribute("errorMsg", "Contraseñas no coinciden");
			return "redirect:/cambiarContrasena";
		}

		return "redirect:/signin";
	}

	// soporte
	@GetMapping("/soporte")
	public String soporte() {

		return "soporte";
	}

	// Asistencia por Video Llamada
	@GetMapping("/soporte/1")
	public String soporte1(Model model) {
		Soporte soporte = soporteService.getSoporteByNombre("Asistencia por Video Llamada");
		model.addAttribute("soporte", soporte);

		return "soporte/1";
	}

	// Consultoría Técnica
	@GetMapping("/soporte/2")
	public String soporte2(Model model) {
		Soporte soporte = soporteService.getSoporteByNombre("Consultoría Técnica");
		model.addAttribute("soporte", soporte);

		return "soporte/2";
	}

	// Diagnóstico Remoto
	@GetMapping("/soporte/3")
	public String soporte3(Model model) {
		Soporte soporte = soporteService.getSoporteByNombre("Diagnóstico Remoto");
		model.addAttribute("soporte", soporte);
		return "soporte/3";
	}

	@GetMapping("/paso2")
	public String paso2(HttpSession session, Model model) {
		Long soporteId = (Long) session.getAttribute("soporteId");

		if (soporteId == null) {
			return "redirect:/soporte";
		}

		List<User> empleados = userService.getUsersBySoporteId(soporteId);
		model.addAttribute("empleados", empleados);

		Soporte soporte = soporteService.getSoporteById(soporteId);
		model.addAttribute("soporte", soporte);
		return "paso2";
	}

	@PostMapping("/paso1")
	public String paso1(@RequestParam Long soporteId, HttpSession session) {
		session.setAttribute("soporteId", soporteId);

		return "redirect:/paso2";
	}

	@PostMapping("/paso2")
	public String paso2(@RequestParam Long empleadoId, @RequestParam String fecha, HttpSession session) {
		session.setAttribute("empleadoId", empleadoId);
		session.setAttribute("fecha", fecha);
		

		return "redirect:/paso3";
	}

	@GetMapping("/paso3")
	public String paso3(HttpSession session, Model model) {
		Long empleadoId = (Long) session.getAttribute("empleadoId");
		String fecha = (String) session.getAttribute("fecha");
		if (empleadoId == null || fecha == null) {
			return "redirect:/paso2";
		}
		User empleado = userService.getUserById(empleadoId);
		model.addAttribute("empleado", empleado);

		// Obtener la fecha seleccionada
		LocalDate fechaSeleccionada = LocalDate.parse(fecha);
		LocalDate fechaActual = LocalDate.now();
		LocalTime horaActual = LocalTime.now();
		// Verificar si la fecha seleccionada es válida
		if (fechaSeleccionada.isBefore(fechaActual)) {
			session.setAttribute("errorMsg", "No puedes seleccionar una fecha pasada.");
			// return "redirect:/paso2"; // Volver a la página anterior
		}
		// Generar horarios disponibles
		List<LocalTime> horarios = generarHorariosValidos(fechaSeleccionada, fechaActual, horaActual);
		List<Cita> citas = citaService.obtenerCitasPorEmpleadoYFecha(empleadoId, fechaSeleccionada);
		// Eliminar los horarios que ya están ocupados
		for (Cita cita : citas) {
			horarios.remove(cita.getHora());
		}
		if (horarios.isEmpty()) {

			session.setAttribute("errorMsg", "No hay horarios disponibles para esta fecha.");
			return "redirect:/paso2"; // Volver a la página anterior si no hay horarios disponibles
		}
		model.addAttribute("horarios", horarios);
		model.addAttribute("fechaSeleccionada", fecha);
		model.addAttribute("empleadoSeleccionado", empleado);

		return "paso3";
	}

	private List<LocalTime> generarHorariosValidos(LocalDate fechaSeleccionada, LocalDate fechaActual,
			LocalTime horaActual) {
		List<LocalTime> horarios = new ArrayList<>();
		LocalTime inicio;

		// Si la fecha es hoy, comenzar desde la próxima hora válida (en intervalos de 2
		// horas)
		if (fechaSeleccionada.equals(fechaActual)) {
			// Si la hora actual es antes de las 8 AM, empezar desde las 8:00 AM
			if (horaActual.isBefore(LocalTime.of(8, 0))) {
				inicio = LocalTime.of(8, 0);
			} else {
				// Si la hora actual es después de las 8 AM, empezar desde el siguiente
				// intervalo de 2 horas
				// Redondeamos la hora actual al próximo intervalo de 2 horas
				int nextInterval = (horaActual.getHour() / 2) * 2; // Redondeo hacia el siguiente intervalo de 2
																	// horas
				if (horaActual.getMinute() > 0)
					nextInterval += 2; // Si ya pasaron los minutos, avanzamos al siguiente intervalo
				if (nextInterval > 23) {
					nextInterval = 23; // Evitar que la hora sea 24
				}
				inicio = LocalTime.of(nextInterval, 0);
			}
		} else {
			// Si es una fecha futura, empezar desde las 8:00 AM
			inicio = LocalTime.of(8, 0);
		}

		// Último horario permitido es a las 6:00 PM
		LocalTime fin = LocalTime.of(18, 0); // 6:00 PM

		// Generar horarios cada 2 horas
		while (!inicio.isAfter(fin)) {
			horarios.add(inicio);
			inicio = inicio.plusHours(2);
		}

		return horarios;
	}
	// Registrar una nueva cita
	@PostMapping("/reservar")
	public String reservar(@RequestParam("empleadoId") Long empleadoId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora, HttpSession session) {
		session.setAttribute("empleadoId", empleadoId);
		session.setAttribute("fecha", fecha);
		session.setAttribute("hora", hora);
		

		// var empleado = empleadoService.obtenerEmpleado(empleadoId);

		/*
		 * Cita cita = new Cita(); cita.setEmpleado(empleado);
		 * cita.setFecha(LocalDate.parse(fecha)); cita.setHora(LocalTime.parse(hora));
		 */

		// citaService.guardarCita(cita);

		return "redirect:/user/reservar";
	}
	

}
