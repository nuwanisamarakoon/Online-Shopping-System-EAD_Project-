package com.example.product_management.controller;

import com.example.product_management.dto.ItemDTO;
import com.example.product_management.dto.ResponseDTO;
import com.example.product_management.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Get an item by its ID.
     *
     * @param id the ID of the item
     * @return the item with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ItemDTO>> getItemById(@PathVariable int id) {
        ResponseDTO<ItemDTO> response = itemService.getItemById(id);
        System.out.println(response.getData());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/order/{id}")
    public ItemDTO getItemByIdForOrder(@PathVariable int id) {
        System.out.println(id);
        ResponseDTO<ItemDTO> response = itemService.getItemById(id);
        return (ItemDTO) response.getData();
    }

    /**
     * Get items by category ID with pagination support.
     *
     * @param categoryId the ID of the category
     * @param pageNo the page number (default is 0)
     * @param pageSize the page size (default is 16)
     * @return a paginated list of items in the specified category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDTO<ItemDTO>> getItemsByCategory(@PathVariable int categoryId,
                                                                   @RequestParam(defaultValue = "0") int pageNo,
                                                                   @RequestParam(defaultValue = "16") int pageSize) {
        ResponseDTO<ItemDTO> response = itemService.getItemsByCategory(categoryId, pageNo, pageSize);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Get all items with pagination support.
     *
     * @param pageNo the page number (default is 0)
     * @param pageSize the page size (default is 16)
     * @return a paginated list of all items
     */
    @GetMapping
    public ResponseEntity<ResponseDTO<ItemDTO>> getItems(@RequestParam(defaultValue = "0") int pageNo,
                                                         @RequestParam(defaultValue = "16") int pageSize) {
        ResponseDTO<ItemDTO> response = itemService.getItems(pageNo, pageSize);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Add a new item to a category.
     *
     * @param item the item to add
     * @param images the images of the item
     * @return the added item
     * @throws IOException if an I/O error occurs
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<ItemDTO>> addItem(@RequestPart("item") ItemDTO item,
                                                        @RequestPart("images") List<MultipartFile> images) throws IOException {
        ResponseDTO<ItemDTO> response = itemService.addItemToCategory(item, images);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Update an existing item.
     *
     * @param id the ID of the item to update
     * @param item the updated item details
     * @param images the updated images of the item
     * @return the updated item
     * @throws IOException if an I/O error occurs
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<ItemDTO>> updateItem(@PathVariable int id,
                                                           @RequestPart("item") ItemDTO item,
                                                           @RequestPart("images") List<MultipartFile> images) throws IOException {
        ResponseDTO<ItemDTO> response = itemService.updateItem(id, item, images);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Delete an item by its ID.
     *
     * @param id the ID of the item to delete
     * @return a response entity with no content if the item is deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<ItemDTO>> deleteItem(@PathVariable int id) {
        ResponseDTO<ItemDTO> response = itemService.deleteItem(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}