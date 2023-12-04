package com.sheryians.major.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity.RequestMatcherConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;


//configuration for authentication such as normal custom and for google authentication 

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
    { 
		
		return new BCryptPasswordEncoder();
    }
	
	
	@Bean
	public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception
	{
//		http
//		.authorizeHttpRequests(authorize-> authorize.requestMatchers("/admin/**")
//				.hasRole("ADMIN")
//				   .requestMatchers("/user/**")
//				   .hasRole("USER")
//					.requestMatchers("/**")
//					.permitAll())
//		       .formLogin(
//		    		   formLogin-> formLogin.loginPage("/signin")
//		    		   .loginProcessingUrl("/dologin")
//		    		   .defaultSuccessUrl("/user/index")
//		    		  // .failureUrl("/login_fail") 
//		    		   )
//		       .csrf((csrf) -> csrf.disable());
//              http.authenticationProvider(authenticationProvider());
//				DefaultSecurityFilterChain defaultSecurityFilterChain=http.build();
//				return defaultSecurityFilterChain;
//		      
          
		http
		.authorizeHttpRequests(
				authorize->authorize.requestMatchers("/","/shop/**","/register").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest()
				.authenticated())
	     .formLogin(
						formLogin->formLogin.loginPage("/login").permitAll()
						.failureUrl("/login?error=true")
						.defaultSuccessUrl("/home")
						.usernameParameter("email")
						.passwordParameter("password")
				        )
				.oauth2Login(
						oauth2Login->oauth2Login.loginPage("/login")
	               	.successHandler(googleOAuth2SuccessHandler)
						)
			      .logout((logout) -> logout.logoutSuccessUrl("/login")
			    			//.logoutRequestMatcher(new AntPathRequestMatcher("/logout")
			    		  .invalidateHttpSession(true)
			    		  .deleteCookies("JSESSIONID")
			    		  )
			     .csrf((csrf) -> csrf.disable());
			      
			      
		
		//http.authenticationProvider(authenticationProvider());
		DefaultSecurityFilterChain defaultSecurityFilterChain=http.build();
		return defaultSecurityFilterChain;
		
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		//in this we have to define which type of authentication we are using  dao, inmemory auth..
		//auth.inMemoryAuthentication();
		auth.userDetailsService(customUserDetailService);
		
	}
	protected void configure(WebSecurity web) throws Exception
	{
		//in this we have to define which type of authentication we are using  dao, inmemory auth..
		//auth.inMemoryAuthentication();
		web.ignoring().requestMatchers("/resources/**","/static/**","/images/**","/productImages/**","/css/**","/js/**");
	
	}
	
}

