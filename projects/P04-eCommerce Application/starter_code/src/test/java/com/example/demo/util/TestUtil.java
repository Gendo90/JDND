package com.example.demo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

public class TestUtil {

	public static Item getFirstItem() {
		Item firstItem = new Item();
		firstItem.setId(1L);
		firstItem.setName("Round Widget");
		firstItem.setPrice(new BigDecimal(2.99));
		firstItem.setDescription("A widget that is round.");
		
		return firstItem;
	}
	
	public static Item getSecondItem() {
		Item secondItem = new Item();
		secondItem.setId(2L);
		secondItem.setName("Square Widget");
		secondItem.setPrice(new BigDecimal(1.99));
		secondItem.setDescription("A widget that is square.");
		
		return secondItem;
	}
	
	public static List<Item> getItemList() {
		List<Item> items = new ArrayList<>();
		
		items.add(getFirstItem());
		items.add(getSecondItem());
		
		return items;
	}
	
	public static User getTestUser() {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Tester");
		
		Cart testCart = new Cart();
		testCart.setId(1L);
		testCart.setUser(testUser);
		
		testUser.setCart(testCart);
		
		
		return testUser;
	}
}
