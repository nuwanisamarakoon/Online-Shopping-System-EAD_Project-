package com.example.order_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.order_management.service.ShoppingCartService;
import com.example.order_management.model.ShoppingCartItem;
import com.example.order_management.dto.ShoppingCartItemDto;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    // Get Shopping Cart Items by User Id for viewing
    @GetMapping("/{userId}")
    public ResponseEntity<List<ShoppingCartItemDto>> getShoppingCartByUserId(@PathVariable int userId) {
        return shoppingCartService.getShoppingCartByUserId(userId)
                .filter(list -> !list.isEmpty()) // Ensures the list is not empty
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update the Shopping Cart Items
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCartItem> updateShoppingCart(@PathVariable int id,
            @RequestParam int updatedQuantity) {
        return ResponseEntity.ok(shoppingCartService.updateShoppingCart(id,
                updatedQuantity));
    }

    // Delete Item from the Shopping Cart
    @DeleteMapping("/{id}")
    public Optional<ShoppingCartItem> deleteItemFromtheShoppingCart(@PathVariable int id) {
        return shoppingCartService.deleteItemFromtheShoppingCart(id);
    }

    @PostMapping("/addItem")
    public ShoppingCartItem createShoppingCartItem(@RequestBody ShoppingCartItem shoppingCartItem) {
        return shoppingCartService.createShoppingCartItem(shoppingCartItem);
    }
}
