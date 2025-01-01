package com.example.order_management.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.order_management.model.ShoppingCartItem;

@DataJpaTest
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository underTestShoppingCartRepository;

    private ShoppingCartItem underTestShoppingCartItem;

    @BeforeEach
    void setUp() {
        // Prepare test data
        underTestShoppingCartItem = new ShoppingCartItem();
        underTestShoppingCartItem.setId(2);
        underTestShoppingCartItem.setItemId(1);
        underTestShoppingCartItem.setQuantity(3);
        underTestShoppingCartItem.setUserId(4);

        // Save the item to the repository
        underTestShoppingCartRepository.save(underTestShoppingCartItem);
    }

    @Test
    void testFindByUserId() {
        // Test that the item can be found by the user ID
        List<ShoppingCartItem> foundShoppingCartItems = underTestShoppingCartRepository
                .findByUserId(underTestShoppingCartItem.getUserId());

        assertFalse(foundShoppingCartItems.isEmpty(), "Shopping cart items should be found for the given user ID");
        assertEquals(1, foundShoppingCartItems.size(), "Only one item should be found for the user");
    }

    @Test
    void testFindByUserIdWhenNoItemsExist() {
        // Test when no shopping cart items exist for a given user ID
        List<ShoppingCartItem> foundShoppingCartItems = underTestShoppingCartRepository
                .findByUserId(999); // Use a non-existent user ID

        assertTrue(foundShoppingCartItems.isEmpty(), "Shopping cart items should be empty for non-existent user ID");
    }

    @Test
    void testFindByUserIdWhenMultipleItemsExist() {
        // Create and save another shopping cart item for the same user
        ShoppingCartItem anotherItem = new ShoppingCartItem();
        anotherItem.setItemId(2);
        anotherItem.setQuantity(5);
        anotherItem.setUserId(underTestShoppingCartItem.getUserId());
        underTestShoppingCartRepository.save(anotherItem);

        // Test that multiple items can be found for the same user ID
        List<ShoppingCartItem> foundShoppingCartItems = underTestShoppingCartRepository
                .findByUserId(underTestShoppingCartItem.getUserId());

        assertEquals(2, foundShoppingCartItems.size(), "Two items should be found for the user");
    }
}
