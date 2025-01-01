package com.example.product_management.controller;

import com.example.product_management.dto.CategoryDTO;
import com.example.product_management.dto.ResponseDTO;
import com.example.product_management.service.AmazonS3Service;
import com.example.product_management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AmazonS3Service amazonS3Service;

    /**
     * Get all categories.
     *
     * @return a list of all categories
     */
    @GetMapping
    public ResponseEntity<ResponseDTO<CategoryDTO>> getAllCategories() {
        ResponseDTO<CategoryDTO> response = categoryService.getAllCategories();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Get a category by its ID.
     *
     * @param id the ID of the category
     * @return the category with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CategoryDTO>> getCategoryById(@PathVariable int id) {
        ResponseDTO<CategoryDTO> response = categoryService.getCategoryById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Create a new category.
     *
     * @param categoryDTO the category to create
     * @param image the image of the category
     * @return the created category
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<CategoryDTO>> createCategory(
            @RequestPart("category") CategoryDTO categoryDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam Integer userId,
            @RequestParam String role
    ) throws IOException {
        ResponseDTO<CategoryDTO> response = categoryService.createCategory(categoryDTO, image);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Update a category by its ID.
     *
     * @param id the ID of the category
     * @param categoryDTO the updated category
     * @param image the updated image of the category
     * @return the updated category
     */
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO<CategoryDTO>> updateCategory(
            @PathVariable int id,
            @RequestPart(value = "category", required = false) CategoryDTO categoryDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam Integer userId,
            @RequestParam String role
    ) throws IOException {
        System.out.println("userId: " + userId);
        System.out.println("role: " + role);

        ResponseDTO<CategoryDTO> response = categoryService.updateCategory(id, categoryDTO, image);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Delete a category by its ID.
     *
     * @param id the ID of the category
     * @return the deleted category
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<CategoryDTO>> deleteCategory(
            @PathVariable int id,
            @RequestParam Integer userId,
            @RequestParam String role
    ) {
        ResponseDTO<CategoryDTO> response = categoryService.deleteCategory(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}