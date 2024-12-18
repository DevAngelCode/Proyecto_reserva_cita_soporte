package com.app.EcommerceInformatico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.EcommerceInformatico.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);

	public List<User> findByRol(String role);

	public User findByResetToken(String Token);



	//lista de usuarios por soporte y isEnable 
	public List<User> findByIsEnableAndSoporteId(Boolean isEnable,Long soporte);
	
	
	

}
