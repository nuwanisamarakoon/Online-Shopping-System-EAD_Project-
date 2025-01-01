# Online Shopping System Backend

## Endpoints

### User Management

#### Register User
- **Endpoint:** `/api/users/register`
- **Method:** `POST`
- **Parameters:**
  - `username` (string, required)
  - `password` (string, required)
  - `email` (string, required)
- **Responses:**
  - `201 Created` on success
  - `400 Bad Request` if parameters are missing or invalid

#### Login User
- **Endpoint:** `/api/users/login`
- **Method:** `POST`
- **Parameters:**
  - `username` (string, required)
  - `password` (string, required)
- **Responses:**
  - `200 OK` with user token on success
  - `401 Unauthorized` if credentials are invalid

### Product Management

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
