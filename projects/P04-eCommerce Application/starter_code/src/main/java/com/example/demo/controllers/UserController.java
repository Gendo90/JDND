package com.example.demo.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) throws Exception {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) throws Exception {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) throws Exception {
		//Uncomment below line to check error logging (divide by zero error example) 
		//Integer n=10/0;
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			//System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
			//		createUserRequest.getUsername());
			if(createUserRequest.getPassword().length()<7) {
				log.error("User creation failed - insufficient password length");
			}
			else {
				log.error("User creation failed - password mismatch");
			}

			return ResponseEntity.badRequest().build();
		}
		// bCryptPasswordEncoder automatically sets a salt for the password
		String hashedPassword = bCryptPasswordEncoder.encode(createUserRequest.getPassword());
		user.setPassword(hashedPassword);
		userRepository.save(user);
		log.info("User " + user.getUsername() + " with id " + user.getId() + " created!"); 
		return ResponseEntity.ok(user);
	}
	
}
