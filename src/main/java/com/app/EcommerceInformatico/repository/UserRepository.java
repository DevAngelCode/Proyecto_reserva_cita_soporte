package com.app.EcommerceInformatico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.EcommerceInformatico.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);

	public List<User> findByRol(String role);

	public User findByResetToken(String Token);

}
