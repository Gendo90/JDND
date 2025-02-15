package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerUnitTest {
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@InjectMocks
	private UserController userController;
	
	@Test
	public void createUser() throws Exception {
		when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
		
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("me");
		createUserRequest.setPassword("testPassword");
		createUserRequest.setConfirmPassword("testPassword");
		
		ResponseEntity<User> result = userController.createUser(createUserRequest);
		User newUser = result.getBody();
		
		assertEquals("me", newUser.getUsername());
		assertEquals("hashedPassword", newUser.getPassword());
		
		Mockito.verify(userRepository).save(any(User.class));
		Mockito.verify(cartRepository).save(any(Cart.class));
	}
	
	@Test
	public void createUserInvalidPasswordLength() throws Exception {
		
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("me");
		createUserRequest.setPassword("abc");
		createUserRequest.setConfirmPassword("abc");
		
		ResponseEntity<User> result = userController.createUser(createUserRequest);
		HttpStatus responseStatus = result.getStatusCode();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
	}
	
	@Test
	public void createUserMismatchPasswords() throws Exception {
		
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("me");
		createUserRequest.setPassword("testPassword");
		createUserRequest.setConfirmPassword("testPassword2");
		
		ResponseEntity<User> result = userController.createUser(createUserRequest);
		HttpStatus responseStatus = result.getStatusCode();
		
		assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
	}
	
	@Test
	public void findById() throws Exception {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Tester");
		
		Optional<User> dbResponse = Optional.of(testUser);
		
		when(userRepository.findById(1L)).thenReturn(dbResponse);
		
		ResponseEntity<User> result = userController.findById(1L);
		User responseUser = result.getBody();
		
		assertEquals(testUser.getUsername(), responseUser.getUsername());
		assertEquals(testUser.getId(), responseUser.getId());
		
		Mockito.verify(userRepository).findById(1L);
	}
	
	@Test
	public void findByUsername() throws Exception {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Tester");
		
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		
		ResponseEntity<User> result = userController.findByUserName("Tester");
		User responseUser = result.getBody();
		
		assertEquals(testUser.getUsername(), responseUser.getUsername());
		assertEquals(testUser.getId(), responseUser.getId());
		
		Mockito.verify(userRepository).findByUsername("Tester");
	}
	
	@Test
	public void findByInvalidUsername() throws Exception {
		String invalidUsername = "fake user";
		when(userRepository.findByUsername(invalidUsername)).thenReturn(null);
		
		ResponseEntity<User> result = userController.findByUserName(invalidUsername);
		HttpStatus responseStatus = result.getStatusCode();
		
		assertEquals(HttpStatus.NOT_FOUND, responseStatus);
	}
}
