package com.app.EcommerceInformatico.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.app.EcommerceInformatico.service.CommomService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Service
public class CommomServiceImpl implements CommomService {

	@Override
	public void removeSessionMessage() {
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
				.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("errorMsg");
		session.removeAttribute("succMsg");

	}

}
