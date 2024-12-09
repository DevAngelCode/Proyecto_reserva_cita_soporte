package com.app.EcommerceInformatico.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.repository.UserRepository;
import com.app.EcommerceInformatico.service.UserService;
import com.app.EcommerceInformatico.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");
		User userDtls = userRepository.findByEmail(email);
		// cuando no existe el gmail
		if (userDtls == null) {
			exception = new LockedException("El correo electrónico no está registrado.");

		} else {
			// Si el usuario existe, pero la contraseña es incorrecta
			if (exception instanceof BadCredentialsException) {
				exception = new BadCredentialsException("Contraseña incorrecta.");
			}
			if (userDtls.getIsEnable()) {
				if (userDtls.getCuentaNoBloqueada()) {
					if (userDtls.getIntentosFallidos() < AppConstant.ATTEMPT_TIME) {
						userService.increaseFailedAttempts(userDtls);
					} else {
						userService.userAccountUnlock(userDtls);
						exception = new LockedException("tu cuenta esta bloqueada. fallo en mas de 3 intentos.");

					}

				} else {
					if (userService.unlockAccountTimeExpired(userDtls)) {
						exception = new LockedException("tu cuenta esta desbloqueada. por favor intenta de nuevo");
					} else {
						exception = new LockedException(
								"tu cuenta esta bloqueada. por favor intenta de nuevo despues de 10 segundos");

					}
				}

			} else {
				exception = new LockedException("tu cuenta esta inactiva. por favor contacta al administrador.");
			}
		}
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
