package com.app.EcommerceInformatico.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;

	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("angelcode159@gmail.com", "Ecommerce Informatico");
		helper.setTo(reciepentEmail);

		String content = "<p>Hola,</p>" + "<p>Has solicitado restablecer tu contraseña.</p>"
				+ "<p>Haz clic en el siguiente enlace para cambiar tu contraseña:</p>" + "<p><a href=\"" + url
				+ "\">Cambiar mi contraseña</a></p>";
		helper.setSubject("Restablecer contraseña");
		helper.setText(content, true);
		mailSender.send(message);

		return true;
	}

	public static String generateUrl(HttpServletRequest request) {

		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), "");

	}
}
