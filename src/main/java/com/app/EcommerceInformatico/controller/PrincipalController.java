package com.app.EcommerceInformatico.controller;

import java.io.IOException;
import java.security.Principal;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;
import com.app.EcommerceInformatico.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PrincipalController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;
	@Autowired
	private UserService userService;

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

}
