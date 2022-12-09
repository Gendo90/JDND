package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.util.TestUtil;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerUnitTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@InjectMocks
	private OrderController orderController;
	
	@Test
	public void orderSubmissionTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		Item testItem = TestUtil.getFirstItem();
		
		// Add existing cart items to cart
		List<Item> existingCartItems = TestUtil.getItemList();
		
		Cart testCart = testUser.getCart();
		existingCartItems.forEach(item -> testCart.addItem(item));
		
		UserOrder testOrder = UserOrder.createFromCart(testCart);
		
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		
		ResponseEntity<UserOrder> result = orderController.submit(testUser.getUsername());
		UserOrder resultOrder = result.getBody();
		
		assertEquals(existingCartItems, resultOrder.getItems());
	}
	
	@Test
	public void orderHistoryTest() {
		// Cart attached to User, initially empty
		User testUser = TestUtil.getTestUser();
		
		Item testItem = TestUtil.getFirstItem();
		
		// Add existing cart items to cart
		List<Item> existingCartItems = TestUtil.getItemList();
		
		Cart testCart = testUser.getCart();
		existingCartItems.forEach(item -> testCart.addItem(item));
		
		List<UserOrder> exampleOrders = new ArrayList<>();
		
		UserOrder testOrder = UserOrder.createFromCart(testCart);
		exampleOrders.add(testOrder);
		
		// remove one item and create a new existing order
		testCart.removeItem(testItem);
		
		UserOrder testOrder2 = UserOrder.createFromCart(testCart);
		exampleOrders.add(testOrder2);
		
		//Add mock outputs for given inputs here
		when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
		when(orderRepository.findByUser(testUser)).thenReturn(exampleOrders);
		
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUser.getUsername());
		List<UserOrder> retrievedOrders = response.getBody();
		
		assertEquals(exampleOrders, retrievedOrders);
	}
}
