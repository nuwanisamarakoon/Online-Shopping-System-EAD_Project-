# Product Management

This module handles all product-related operations in the Online Shopping System.

## Features

- Add, update, delete, and retrieve products
- Manage product categories
- Upload product images to Amazon S3

## Endpoints

### Product Endpoints

#### Get All Products
- **Endpoint:** `/api/products`
- **Method:** `GET`
- **Parameters:** None
- **Responses:**
  - `200 OK` with list of products

#### Get Product by ID
- **Endpoint:** `/api/products/{id}`
- **Method:** `GET`
- **Parameters:**
  - `id` (string, required)
- **Responses:**
  - `200 OK` with product details
  - `404 Not Found` if product does not exist

#### Add New Product
- **Endpoint:** `/api/products`
- **Method:** `POST`
- **Parameters:**
  - `name` (string, required)
  - `price` (number, required)
  - `description` (string, optional)
- **Responses:**
  - `201 Created` on success
  - `400 Bad Request` if parameters are missing or invalid

#### Update Product
- **Endpoint:** `/api/products/{id}`
- **Method:** `PUT`
- **Parameters:**
  - `id` (string, required)
  - `name` (string, optional)
  - `price` (number, optional)
  - `description` (string, optional)
- **Responses:**
  - `200 OK` on success
  - `400 Bad Request` if parameters are invalid
  - `404 Not Found` if product does not exist

#### Delete Product
- **Endpoint:** `/api/products/{id}`
- **Method:** `DELETE`
- **Parameters:**
  - `id` (string, required)
- **Responses:**
  - `200 OK` on success
  - `404 Not Found` if product does not exist

#### Get Items by Category
- **Endpoint:** `/api/items/category/{categoryId}`
- **Method:** `GET`
- **Parameters:**
  - `categoryId` (string, required)
  - `pageNo` (int, optional, default: 0)
  - `pageSize` (int, optional, default: 16)
- **Responses:**
  - `200 OK` with list of items in the specified category
  - `404 Not Found` if category does not exist

### Category Endpoints

#### Get All Categories
- **Endpoint:** `/api/categories`
- **Method:** `GET`
- **Parameters:** None
- **Responses:**
  - `200 OK` with list of categories

#### Get Category by ID
- **Endpoint:** `/api/categories/{id}`
- **Method:** `GET`
- **Parameters:**
  - `id` (string, required)
- **Responses:**
  - `200 OK` with category details
  - `404 Not Found` if category does not exist

#### Create Category
- **Endpoint:** `/api/categories`
- **Method:** `POST`
- **Parameters:**
  - `category` (CategoryDTO, required)
  - `image` (MultipartFile, optional)
- **Responses:**
  - `201 Created` on success
  - `400 Bad Request` if parameters are missing or invalid

#### Update Category
- **Endpoint:** `/api/categories/{id}`
- **Method:** `PUT`
- **Parameters:**
  - `id` (string, required)
  - `category` (CategoryDTO, optional)
  - `image` (MultipartFile, optional)
- **Responses:**
  - `200 OK` on success
  - `400 Bad Request` if parameters are invalid
  - `404 Not Found` if category does not exist

#### Delete Category
- **Endpoint:** `/api/categories/{id}`
- **Method:** `DELETE`
- **Parameters:**
  - `id` (string, required)
- **Responses:**
  - `200 OK` on success
  - `404 Not Found` if category does not exist


