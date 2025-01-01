package com.example.order_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.order_management.model.ShoppingCartItem;
import com.example.order_management.repository.ShoppingCartRepository;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository underTestShoppingCartRepository;

    @InjectMocks
    private ShoppingCartService underTestShoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShoppingCartItem() {
        ShoppingCartItem underTestShoppingCartItem = new ShoppingCartItem();

        underTestShoppingCartItem.setId(1);
        underTestShoppingCartItem.setItemId(2);
        underTestShoppingCartItem.setQuantity(3);
        underTestShoppingCartItem.setUserId(6);

        when(underTestShoppingCartRepository.save(any(ShoppingCartItem.class))).thenReturn(underTestShoppingCartItem);
        ShoppingCartItem result = underTestShoppingCartService.createShoppingCartItem(underTestShoppingCartItem);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(2, result.getItemId());
        assertEquals(3, result.getQuantity());
        assertEquals(6, result.getUserId());

    }

    @Test
    void testDeleteItemFromtheShoppingCart() {
        ShoppingCartItem mockItem = new ShoppingCartItem(2, 3, 6);
        int cartItemId = mockItem.getId();

        // Mocking behavior
        when(underTestShoppingCartRepository.findById(cartItemId))
                .thenReturn(Optional.of(mockItem));

        // Call the method under test
        Optional<ShoppingCartItem> result = underTestShoppingCartService.deleteItemFromtheShoppingCart(cartItemId);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(mockItem, result.get());

        // Verify that deleteById was called
        verify(underTestShoppingCartRepository).deleteById(cartItemId);
    }

    @Test
    void testUpdateShoppingCart() {
        int itemId = 999;
        int updatedQuantity = 10;
        // ShoppingCartItem updatedItemDetails = new ShoppingCartItem();

        when(underTestShoppingCartRepository.findById(itemId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            underTestShoppingCartService.updateShoppingCart(itemId, updatedQuantity);
        });
        assertEquals("Shopping Cart Item Not Found", exception.getMessage());
        verify(underTestShoppingCartRepository, times(1)).findById(itemId);
        verify(underTestShoppingCartRepository, times(0)).save(any());
    }
}
