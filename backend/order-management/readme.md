## API End Points

### Shopping Cart API calls

### Get Shopping Cart Items by User ID

- **URL:** `/api/shoppingCart/{userId}`
- **Method:** `GET`
- **Response Body:**
  ```json
  [
  {
    "id": 13,
    "itemName": "T-shirt",
    "itemPrice": 25.50,
    "itemQuantity": 2,
    "imageURL": "https://ead-propics.s3.ap-south-1.amazonaws.com/products/item_36130.jpeg"
  }
  ]
  ```

### Get all Items (Admin)

- **URL:** `/api/order`
- **Method:** `GET`
- **Request Body:**
  ```json
  {
  "id": 1,
  "itemId": 101,
  "quantity": 3,
  "price": 75.00,
  "userId": 10
  }
  ```

###  Delete an order 

- **URL:** `/api/order`
- **Method:** `DELETE`

### Update an Order

- **URL:** `/api/order`
- **Method:** `PUT`


