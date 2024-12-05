package com.app.EcommerceInformatico.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;
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
	private CommonUtil commonUtil;

	@ModelAttribute
	public void getUserDetails(Principal p, Model model) {
		if (p != null) {
			String email = p.getName();
			User userDtls = userService.getUserByEmail(email);
			model.addAttribute("user", userDtls);
			//Integer countCart = cartService.getCounrCart(userDtls.getId());
			//model.addAttribute("countCart", countCart);

		}
		//List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		//model.addAttribute("categorys", allActiveCategory);
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("categorias", categoriaService.getAllActiveCategoria());
		model.addAttribute("productos", productoService.getAllActiveProducto());
		return "home";
	}

	@GetMapping("/signin")
	public String inicioSesion() {
		return "signin";
	}

	// save user
	@PostMapping("/registrar")
	public String saveUser(@ModelAttribute User user, HttpSession session) throws IOException {
		User saveUser = userService.saveUser(user);
		if (!ObjectUtils.isEmpty(saveUser)) {

			session.setAttribute("succMsg", "Usuario registrado con éxito");

		} else {
			session.setAttribute("errorMsg", "Algo salió mal");

		}
		return "redirect:/signin";
	}
	
	//recuperar_contraseña
	@GetMapping("/recuperar")
	public String recuperar() {
		return "recuperar_contraseña";
	}
	@PostMapping("/recuperar")
	public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		User userByEmail = userService.getUserByEmail(email);
		if (ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "Usuario no encontrado");
		} else {
			String resetToken = UUID.randomUUID().toString();
			userService.updateUserResetToken(email, resetToken);
			// generate reset password link
			// http://localhost:8080/reset-password?token=dsaddsa
			String url = CommonUtil.generateUrl(request) + "/cambiar_contraseña?token=" + resetToken;
			// send email to user with token
			Boolean sendMail = commonUtil.sendMail(url, email);
			if (sendMail) {
				session.setAttribute("succMsg", "Enlace de restablecimiento de contraseña enviado a su correo");
			} else {
				session.setAttribute("errorMsg", "Algo salió mal");
			}
		}
		return "redirect:/recuperar";

	}
	//reset_password
	@GetMapping("/cambiar_contraseña")
	public String resetPassword(@RequestParam String token, HttpSession session, Model m) {

		User userByToken = userService.getUserByToken(token);

		if (userByToken == null) {
			m.addAttribute("msg", "tu link es invalido o ha expirado !!");
			return "mensaje";
		}
		m.addAttribute("token", token);
		return "cambiar_contraseña";
	}

}
