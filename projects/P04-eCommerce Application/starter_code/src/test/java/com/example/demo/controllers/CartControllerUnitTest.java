package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.util.TestUtil;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerUnitTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private ItemRepository itemRepository;
	
	@InjectMocks
	private CartController cartController;
	
	@Test
	public void addToCartTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		Item testItem = TestUtil.getFirstItem();
		
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(testUser.getUsername());
		cartRequest.setItemId(testItem.getId());
		cartRequest.setQuantity(2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
		
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		Cart resultCart = response.getBody();
		
		assertEquals(2, resultCart.getItems().size());
		BigDecimal expectedCartTotal = testItem.getPrice().multiply(new BigDecimal(cartRequest.getQuantity()));
		assertEquals(expectedCartTotal, resultCart.getTotal());
	}
	
	@Test
	public void addToCartNoUserTest() {
		String invalidUsername = "me";
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(invalidUsername);
		cartRequest.setItemId(1L);
		cartRequest.setQuantity(2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(invalidUsername)).thenReturn(null);
		
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		HttpStatus responseStatus = response.getStatusCode();
		
		assertEquals(HttpStatus.NOT_FOUND, responseStatus);
	}
	
	@Test
	public void addToCartNoItemTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		// Invalid item id - so no item found in DB
		Long invalidItemId = 3L;
		
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(testUser.getUsername());
		cartRequest.setItemId(invalidItemId);
		cartRequest.setQuantity(2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		when(itemRepository.findById(invalidItemId)).thenReturn(Optional.empty());
		
		ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
		HttpStatus responseStatus = response.getStatusCode();
		
		assertEquals(HttpStatus.NOT_FOUND, responseStatus);
	}
	
	@Test
	public void removeFromCartTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		Item testItem = TestUtil.getFirstItem();
		
		// Add existing cart items to cart
		List<Item> existingCartItems = TestUtil.getItemList();
		
		Cart testCart = testUser.getCart();
		existingCartItems.forEach(item -> testCart.addItem(item));
		
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(testUser.getUsername());
		cartRequest.setItemId(testItem.getId());
		cartRequest.setQuantity(1);
		
		BigDecimal expectedCartTotal = testCart.getTotal().subtract(testItem.getPrice().multiply(new BigDecimal(cartRequest.getQuantity())));
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
		
		ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
		Cart resultCart = response.getBody();
		
		assertEquals(1, resultCart.getItems().size());
		
		assertEquals(expectedCartTotal, resultCart.getTotal());
	}
	
	@Test
	public void removeFromCartNoUserTest() {
		String invalidUsername = "me";
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(invalidUsername);
		cartRequest.setItemId(1L);
		cartRequest.setQuantity(2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(invalidUsername)).thenReturn(null);
		
		ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
		HttpStatus responseStatus = response.getStatusCode();
		
		assertEquals(HttpStatus.NOT_FOUND, responseStatus);
	}
	
	@Test
	public void removeFromCartNoItemTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		// Invalid item id - so no item found in DB
		Long invalidItemId = 3L;
		
		ModifyCartRequest cartRequest = new ModifyCartRequest();
		cartRequest.setUsername(testUser.getUsername());
		cartRequest.setItemId(invalidItemId);
		cartRequest.setQuantity(2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		when(itemRepository.findById(invalidItemId)).thenReturn(Optional.empty());
		
		ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
		HttpStatus responseStatus = response.getStatusCode();
		
		assertEquals(HttpStatus.NOT_FOUND, responseStatus);
	}
}
