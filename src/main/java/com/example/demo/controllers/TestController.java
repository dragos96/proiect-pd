package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Course;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;







@RestController
@RequestMapping("/api/test")
//@RequestMapping("/users")
public class TestController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/all") 
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}
	
	@GetMapping("/byid/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") Long id){
		Optional<User> userOp = userRepository.findById(id);
		if(userOp.isPresent()){
			User user = userOp.get();
			return ResponseEntity.ok(user);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/save")
	public ResponseEntity<User> save(@RequestBody User user, Principal principal){

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User savedUser = userRepository.save(user);
		return ResponseEntity.ok(savedUser);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id, Principal principal){
		
		Optional<User> opUser = userRepository.findById(id);
		if(!opUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		User user = opUser.get();
		userRepository.delete(user);
		return ResponseEntity.ok(user);
		
	}
	
	@GetMapping("/test")
	public String getTest(Principal principal) {
		return principal.getName();
	}

}
