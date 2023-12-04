package com.sheryians.major.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail extends User implements UserDetails{
	
	public  CustomUserDetail(User user) 
	{
		super(user);
		//user defination is saved in user thats why we done this
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		//working with autgherities 
		//for one particular user what roles should be assinged
		// TODO Auto-generated method stub
		
		List<GrantedAuthority> authorityList=new ArrayList<>();
		//roles are stored in user
		super.getRoles().forEach(role->{
			authorityList.add(new SimpleGrantedAuthority(role.getName()));
			//role must be converted to GrantedAuthority implemented class to store and return --->SimpleGrantedAuthority
		});
		return authorityList;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getEmail();
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		//edited false to true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}



}
