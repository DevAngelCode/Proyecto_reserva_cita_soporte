package com.app.EcommerceInformatico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private LoginMetricsService loginMetricsService;

    public boolean authenticate(String username, String password) {
        boolean isAuthenticated = false;

        // Simula la lógica de autenticación
        if ("user".equals(username) && "password".equals(password)) {
            isAuthenticated = true;
            loginMetricsService.incrementSuccess();
        } else {
            loginMetricsService.incrementFailure();
        }

        return isAuthenticated;
    }
}