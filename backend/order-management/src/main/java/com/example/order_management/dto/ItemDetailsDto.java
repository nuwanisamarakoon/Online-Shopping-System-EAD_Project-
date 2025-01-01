package com.example.order_management.dto;

public class ItemDetailsDto {

    private int id;
    private String name;
    private String description;
    private float price;
    private int quantity;
    private String imageURL;
    private int categoryId;

    public ItemDetailsDto() {
    }

    public ItemDetailsDto(int id, String name, String description, float price, int quantity,String imageURL, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public String getImageURL(){
        return this.imageURL;
    }
    

}
