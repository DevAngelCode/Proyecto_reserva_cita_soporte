package com.app.EcommerceInformatico.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    // Este método se ejecuta cuando ocurre un error
    @RequestMapping("/error")
    public String handleError() {
        // Redirige a la página error/404.html cuando ocurre un error
        return "error/404";
    }

    public String getErrorPath() {
        return "/error"; // Esta es la ruta predeterminada de Spring Boot para errores
    }
}
