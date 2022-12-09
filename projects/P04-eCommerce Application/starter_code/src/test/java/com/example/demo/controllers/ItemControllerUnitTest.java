package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.util.TestUtil;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerUnitTest {

	@Mock
	private ItemRepository itemRepository;
	
	@InjectMocks
	private ItemController itemController;
	
	@Test
	public void getItemsTest() {
		List<Item> availableItems = TestUtil.getItemList();
		
		when(itemRepository.findAll()).thenReturn(availableItems);
		
		ResponseEntity<List<Item>> result = itemController.getItems();
		List<Item> resultItems = result.getBody();
		
		assertEquals(availableItems, resultItems);
		
		verify(itemRepository).findAll();
	}
	
	@Test
	public void getItemByIdTest() {
		Item firstItem = TestUtil.getFirstItem();
		Long id = firstItem.getId();
		
		when(itemRepository.findById(id)).thenReturn(Optional.of(firstItem));
		
		ResponseEntity<Item> result = itemController.getItemById(id);
		Item resultItem = result.getBody();
		
		assertEquals(firstItem, resultItem);
		
		verify(itemRepository).findById(id);
	}
	
	@Test
	public void getItemByNameTest() {
		Item firstItem = TestUtil.getFirstItem();
		String name = firstItem.getName();
		
		when(itemRepository.findByName(name)).thenReturn(Arrays.asList(firstItem));
		
		ResponseEntity<List<Item>> result = itemController.getItemsByName(name);
		Item resultItem = result.getBody().get(0);
		
		assertEquals(firstItem, resultItem);
		
		verify(itemRepository).findByName(name);
	}
}
