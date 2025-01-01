package com.example.order_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.order_management.model.ShoppingCartItem;
import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartItem, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM shopping_cart_item WHERE user_id = :userId;")
    List<ShoppingCartItem> findByUserId(@Param("userId") int userId);

    // @Modifying
    // @Query(nativeQuery = true, value = "DELETE FROM shopping_cart_item WHERE
    // user_id = :userId AND item_id = :id")
    // void deleteById(@Param("userId") int userId, @Param("id") int id);
}
