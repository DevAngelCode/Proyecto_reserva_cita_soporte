package com.app.EcommerceInformatico.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.repository.UserRepository;
import com.app.EcommerceInformatico.service.UserService;
import com.app.EcommerceInformatico.util.AppConstant;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user) {
		user.setRol("ROLE_USER");
		user.setIsEnable(true);
		user.setCuentaNoBloqueada(true);
		user.setIntentosFallidos(0);
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		User saveUser = userRepository.save(user);
		return saveUser;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getUsers(String role) {
		// TODO Auto-generated method stub
		return userRepository.findByRol(role);
	}

	@Override
	public Boolean updateAccountStatus(Boolean status, Long id) {
		Optional<User> findByUser = userRepository.findById(id);
		if (findByUser.isPresent()) {
			User userDtls = findByUser.get();
			userDtls.setIsEnable(status);
			userRepository.save(userDtls);
			return true;
		}

		return false;
	}

	@Override
	public void increaseFailedAttempts(User user) {
		int attempt = user.getIntentosFallidos() + 1;
		user.setIntentosFallidos(attempt);
		userRepository.save(user);

	}

	@Override
	public void userAccountUnlock(User user) {
		user.setCuentaNoBloqueada(false);
		user.setTiempoBloqueo(new Date());
		userRepository.save(user);

	}

	@Override
	public Boolean unlockAccountTimeExpired(User user) {
		long lockTime = user.getTiempoBloqueo().getTime();
		long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
		long currentTime = System.currentTimeMillis();
		if (unlockTime < currentTime) {
			user.setCuentaNoBloqueada(true);
			user.setIntentosFallidos(0);
			user.setTiempoBloqueo(null);
			userRepository.save(user);
			return true;
		}
		return false;

	}

	@Override
	public void resetAttempt(long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {

		User findByEmail = userRepository.findByEmail(email);

		findByEmail.setResetToken(resetToken);
		userRepository.save(findByEmail);
	}

	@Override
	public User getUserByToken(String Token) {
		// TODO Auto-generated method stub
		return userRepository.findByResetToken(Token);

	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);

	}

	@Override
	public List<User> getAllEmpleado() {
		// TODO Auto-generated method stub
		return userRepository.findByRol("ROLE_EMPLOYEE");
	}

	@Override
	public User getUserById(Long id) {
		User user = userRepository.findById(id).get();

		return user;
	}

	//empleados
	@Override
	public List<User> getUsersBySoporteId(Long id) {
		List<User> empleados = userRepository.findBySoporteId(id);
		return empleados;
	}

}
