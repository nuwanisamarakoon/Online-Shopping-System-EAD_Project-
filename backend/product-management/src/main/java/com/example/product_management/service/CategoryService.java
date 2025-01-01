package com.example.product_management.service;

import com.example.product_management.dto.CategoryDTO;
import com.example.product_management.dto.ResponseDTO;
import com.example.product_management.model.Category;
import com.example.product_management.repository.CategoryRepository;
import com.example.product_management.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AmazonS3Service amazonS3Service;

    public ResponseDTO<CategoryDTO> getAllCategories() {
        ResponseDTO<CategoryDTO> response = new ResponseDTO<>();
        try {
            List<CategoryDTO> categories = categoryRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.setStatus(200);
            response.setMessage("Success");
            response.setData(categories);
            return response;
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error: " + e.getMessage());
            response.setData(null);
            return response;
        }
    }

    public ResponseDTO<CategoryDTO> getCategoryById(int id) {
        ResponseDTO<CategoryDTO> response = new ResponseDTO<>();
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            response.setStatus(200);
            response.setMessage("Success");
            response.setData(convertToDTO(category.get()));
            return response;
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error: " + e.getMessage());
            response.setData(null);
            return response;
        }
    }

    public ResponseDTO<CategoryDTO> createCategory(CategoryDTO categoryDTO, MultipartFile image) {
        ResponseDTO<CategoryDTO> response = new ResponseDTO<>();
        try {
            Category category = convertToEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(category);
            response.setStatus(201);
            response.setMessage("Category created successfully");

            if (image != null) {
                String imageUrl = amazonS3Service.uploadFile(image, savedCategory.getId(), "category");
                savedCategory.setImageURL(imageUrl);
                categoryRepository.save(savedCategory);
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setError("Error while creating category");
            response.setData(null);
            return response;
        }
    }

    public ResponseDTO<CategoryDTO> updateCategory(int id, CategoryDTO categoryDTO, MultipartFile image) {
        ResponseDTO<CategoryDTO> response = new ResponseDTO<>();
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            Category categoryToUpdate = category.get();
            if (categoryDTO.getName() != null) {
                categoryToUpdate.setName(categoryDTO.getName());
            }
            if (categoryDTO.getDescription() != null) {
                categoryToUpdate.setDescription(categoryDTO.getDescription());
            }
            if (categoryDTO.getImageURL() != null) {
                categoryToUpdate.setImageURL(categoryDTO.getImageURL());
            }
            Category updatedCategory = categoryRepository.save(categoryToUpdate);

            if (image != null) {
                String imageUrl = amazonS3Service.uploadFile(image, updatedCategory.getId(), "category");
                updatedCategory.setImageURL(imageUrl);
                categoryRepository.save(updatedCategory);
            }

            response.setStatus(200);
            response.setMessage("Category updated successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setError("Error while updating category");
            response.setData(null);
            return response;
        }
    }

    public ResponseDTO<CategoryDTO> deleteCategory ( int id){
        ResponseDTO<CategoryDTO> response = new ResponseDTO<>();
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            categoryRepository.deleteById(id);
            response.setStatus(200);
            response.setMessage("Category deleted successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setError("Error while deleting category");
            response.setData(null);
            return response;
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setImageURL(category.getImageURL());
        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageURL(dto.getImageURL());
        return category;
    }

    public CategoryDTO patchCategory(int id, CategoryDTO categoryDTO) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            Category categoryToUpdate = category.get();
            if (categoryDTO.getName() != null) {
                categoryToUpdate.setName(categoryDTO.getName());
            }
            if (categoryDTO.getDescription() != null) {
                categoryToUpdate.setDescription(categoryDTO.getDescription());
            }
            if (categoryDTO.getImageURL() != null) {
                categoryToUpdate.setImageURL(categoryDTO.getImageURL());
            }
            Category updatedCategory = categoryRepository.save(categoryToUpdate);
            return convertToDTO(updatedCategory);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}



