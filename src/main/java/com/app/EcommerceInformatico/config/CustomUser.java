package com.app.EcommerceInformatico.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.EcommerceInformatico.model.User;


public class CustomUser implements UserDetails {
	private User user;
	
	

	public CustomUser(User user) {
		super();
		this.user = user;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRol());
		return Arrays.asList(authority);
	}



	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}



	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return user.getCuentaNoBloqueada();
	}



	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.getIsEnable();
	}
	
	
}