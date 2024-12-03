package com.app.EcommerceInformatico.service;

import java.util.List;

import com.app.EcommerceInformatico.model.User;


public interface UserService {

	public User saveUser(User user);
	public User getUserByEmail(String email);
	public List<User> getUsers(String role);
	public Boolean updateAccountStatus(Boolean status, Long id);
	public void increaseFailedAttempts(User user);
	public void userAccountUnlock(User user);
	public Boolean unlockAccountTimeExpired(User user);
	public void resetAttempt(long userId);
	public void updateUserResetToken(String email, String resetToken);
	public User getUserByToken(String Token);
	public User updateUser(User user);
}
