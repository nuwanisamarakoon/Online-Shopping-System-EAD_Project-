package com.example.product_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

// import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.product_management.dto.ItemDTO;
import com.example.product_management.dto.ResponseDTO;
import com.example.product_management.model.Item;
import com.example.product_management.repository.ItemRepository;

// @Disabled
public class ItemServiceTest {
    @Mock
    private ItemRepository underTestItemRepository;
    // private AutoCloseable autoCloseable;
    @InjectMocks
    private ItemService underTestItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItemById() {
        Optional<Item> foundItem = underTestItemRepository.findById(1);

        ResponseDTO<ItemDTO> testResponse = underTestItemService.getItemById(1);

        if (foundItem.isEmpty()) {
            assertEquals(500, testResponse.getStatus());
        } else {
            assertEquals(200, testResponse.getStatus());
        }

    }

    @Test
    void testDeleteItem() {
        Optional<Item> foundItem = underTestItemRepository.findById(1);
        ResponseDTO<ItemDTO> testResponse = underTestItemService.deleteItem(1);
        if (foundItem.isEmpty()) {
            assertEquals(500, testResponse.getStatus());
        } else {
            assertEquals(200, testResponse.getStatus());
        }

    }

    @Test
    void testGetItems() {
        Pageable testPageable = PageRequest.of(0, 2);
        Page<Item> foundItems = underTestItemRepository.findAll(testPageable);
        ResponseDTO<ItemDTO> testResponse = underTestItemService.getItems(0, 2);

        if (foundItems == null) {
            assertEquals(500, testResponse.getStatus());
        } else {
            assertEquals(200, testResponse.getStatus());
        }

    }

    @Test
    void testGetItemByCategory() {
        Pageable testPageable = PageRequest.of(0, 2);
        Page<Item> foundItems = underTestItemRepository.findByCategoryId(1, testPageable);
        ResponseDTO<ItemDTO> testResponse = underTestItemService.getItemsByCategory(1, 0, 2);

        if (foundItems == null) {
            assertEquals(500, testResponse.getStatus());
        } else {
            assertEquals(200, testResponse.getStatus());
        }

    }
}
