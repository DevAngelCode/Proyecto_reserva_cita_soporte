package com.app.EcommerceInformatico.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@ModelAttribute
	public void getUserDetails(Principal p, Model model) {
		if (p != null) {
			String email = p.getName();
			User userDtls = userService.getUserByEmail(email);
			model.addAttribute("user", userDtls);
			// Integer countCart = cartService.getCounrCart(userDtls.getId());
			// model.addAttribute("countCart", countCart);

		}
		// List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		// model.addAttribute("categorys", allActiveCategory);
	}

}
