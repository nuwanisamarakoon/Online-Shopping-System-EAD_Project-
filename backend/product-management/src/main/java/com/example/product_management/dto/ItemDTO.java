package com.example.product_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Integer categoryId;
    private String imageURL;
    private List<String> otherImageURLs;
}
