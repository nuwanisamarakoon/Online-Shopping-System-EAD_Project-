package com.example.product_management.repository;

import com.example.product_management.model.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Integer> {
    List<ItemImage> findByItemId(int id);
}