package com.example.product_management.repository;

import com.example.product_management.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

//@Repository
//public interface ItemRepository extends JpaRepository<Item, Integer> {
//    @Query(nativeQuery = true, value = "SELECT * FROM items WHERE categoryId=:categoryId")
//    List<Item> findByCategoryId(@Param("categoryId") int categoryId);
//}

//@Repository
//public interface ItemRepository extends JpaRepository<Item, Integer> {
//}





//@Repository
//public interface ItemRepository extends JpaRepository<Item, Integer> {
//    Page<Item> findByCategoryId(int categoryId, Pageable pageable);
//    List<Item> findByCategoryId(int categoryId);
//}

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    List<Item> findByCategoryId(@Param("categoryId") int categoryId);

    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);
}
