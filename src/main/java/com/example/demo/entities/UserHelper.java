package com.example.demo.entities;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.UserRepository;


@Service
public class UserHelper {
	
	@Autowired
	private UserRepository repoUser;
	
	public Optional<User> getUserFromPrincipal(Principal principal) {
		Optional<User> opUser = repoUser.findByUsername(principal.getName());
		
		return opUser;
	}

}
