package com.example.order_management.controller;

import com.example.order_management.dto.ShoppingCartItemDto;
import com.example.order_management.model.ShoppingCartItem;
import com.example.order_management.service.ShoppingCartService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Test
    void testGetShoppingCartByUserId() throws Exception {
        // Arrange
        ShoppingCartItemDto item1 = new ShoppingCartItemDto(1, "Item 1", 20, 2,"");
        ShoppingCartItemDto item2 = new ShoppingCartItemDto(2, "Item 2", 10, 10, "");
        List<ShoppingCartItemDto> items = Arrays.asList(item1, item2);

        when(shoppingCartService.getShoppingCartByUserId(1)).thenReturn(Optional.of(items));

        // Act & Assert
        mockMvc.perform(get("/api/shoppingCart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetShoppingCartByUserId_NotFound() throws Exception {
        // Arrange
        when(shoppingCartService.getShoppingCartByUserId(1)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/shoppingCart/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateShoppingCart() throws Exception {
        // Arrange
        ShoppingCartItem updatedItem = new ShoppingCartItem(1, 2, 3);
        when(shoppingCartService.updateShoppingCart(1, 3)).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(put("/api/shoppingCart/1")
                        .param("updatedQuantity", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.itemId").value(2))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void testDeleteItemFromShoppingCart() throws Exception {
        // Arrange
        ShoppingCartItem deletedItem = new ShoppingCartItem(1, 1, 2);
        when(shoppingCartService.deleteItemFromtheShoppingCart(1)).thenReturn(Optional.of(deletedItem));

        // Act & Assert
        mockMvc.perform(delete("/api/shoppingCart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.name").value("Item 1"));
    }

    @Test
    void testDeleteItemFromShoppingCart_NotFound() throws Exception {
        // Arrange
        when(shoppingCartService.deleteItemFromtheShoppingCart(1)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/shoppingCart/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testCreateShoppingCartItem() throws Exception {
        // Arrange
        ShoppingCartItem newItem = new ShoppingCartItem(4, 1, 2);
        when(shoppingCartService.createShoppingCartItem(any(ShoppingCartItem.class))).thenReturn(newItem);

        // Act & Assert
        mockMvc.perform(post("/api/shoppingCart/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":4,\"itemId\":1,\"quantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(4))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.quantity").value(2));
    }
}
