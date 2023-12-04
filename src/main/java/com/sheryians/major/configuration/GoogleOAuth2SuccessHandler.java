package com.sheryians.major.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sheryians.major.model.Role;
import com.sheryians.major.model.User;
import com.sheryians.major.repository.RoleRepository;
import com.sheryians.major.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

@Component //bcoz we need obj of this class and this is class and we need object of this 
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	// we can also use this manully when ever we want to redirect 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		// in if authentication is succesfully then then we bring oauth token with us
		// google verifys user and gives token 
		// and we will use it 
		
//		which gets from authentication 
		
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		
		// first we will check if user is already present in database 
		String email= token.getPrincipal().getAttributes().get("email").toString();
		
		if(userRepository.findUserByEmail(email).isPresent()) 
		{
			// no need to do any action if user is alrady present
		}
		else 
		{
			User user = new User();
			user.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString());
			user.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
			user.setEmail(email);
			//no need for password
			List<Role> roles = new ArrayList<>();
			roles.add(roleRepository.findById(2).get());
			// here in role table only to roles are their admin and user so we will fetch user role
			user.setRoles(roles);
			
			userRepository.save(user);
		}
		//now we have to redirect 
		redirectStrategy.sendRedirect(request, response, email);
		
		
	}
	
}
