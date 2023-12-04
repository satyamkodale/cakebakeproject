package com.sheryians.major.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sheryians.major.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	// here we have to create a method which returns user based on id that is in our case email
	
	Optional<User>findUserByEmail(String email);

}
