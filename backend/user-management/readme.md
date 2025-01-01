# User Management Service

This service handles user management functionalities such as user registration, authentication, profile management, and role management.

## Endpoints

### AuthController

#### POST /auth/signup

Registers a new user.

**Request Body:**
- `email` (String): The user's email address.
- `password` (String): The user's password.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### POST /auth/signIn

Authenticates a user and generates tokens.

**Request Body:**
- `email` (String): The user's email address.
- `password` (String): The user's password.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.
- `token` (String): The generated JWT token.
- `refreshToken` (String): The generated refresh token.
- `expirationTime` (String): The expiration time of the token.
- `userId` (int): The ID of the authenticated user.
- `email` (String): The email of the authenticated user.
- `role` (String): The role of the authenticated user.

#### POST /auth/token/refresh

Refreshes the authentication token.

**Request Body:**
- `refreshToken` (String): The refresh token.

**Response:**
- `statusCode` (int): The status code of the response.
- `token` (String): The new JWT token.
- `refreshToken` (String): The refresh token.
- `expirationTime` (String): The expiration time of the token.
- `message` (String): A message indicating the result of the operation.

#### POST /auth/verify-token

Verifies the authentication token.

**Request Body:**
- `token` (String): The token to verify.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.
- `userId` (int): The ID of the authenticated user.
- `role` (String): The role of the authenticated user.
- `accStatus` (String): The account status of the authenticated user.

### UserController

#### GET /users

Retrieves all user profiles.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Response:**
- `statusCode` (int): The status code of the response.
- `data` (List<UserProfile>): A list of user profiles.

#### GET /users/{id}/profile

Retrieves a user profile by ID.

**Path Variables:**
- `id` (int): The ID of the user profile to retrieve.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Response:**
- `statusCode` (int): The status code of the response.
- `data` (UserProfile): The user profile.

#### POST /users/{id}/profile

Creates a new user profile.

**Path Variables:**
- `id` (int): The ID of the user profile to create.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Request Parts:**
- `userProfile` (UserProfile): The user profile data.
- `profilePicture` (MultipartFile): The profile picture file.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### PUT /users/{id}/profile

Updates an existing user profile.

**Path Variables:**
- `id` (int): The ID of the user profile to update.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Request Parts:**
- `userProfileDetails` (UserProfile): The updated user profile data.
- `profilePicture` (MultipartFile): The updated profile picture.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### DELETE /users/{id}/profile

Deletes a user profile.

**Path Variables:**
- `id` (int): The ID of the user profile to delete.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### PUT /users/{id}/password

Changes the password of a user.

**Path Variables:**
- `id` (int): The ID of the user whose password is to be changed.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Request Body:**
- `currentPassword` (String): The current password of the user.
- `newPassword` (String): The new password of the user.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### POST /users/verify-email

Verifies the email of a user.

**Request Body:**
- `email` (String): The email of the user.
- `verificationCode` (String): The verification code sent to the user's email.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.

#### PUT /users/{id}/role

Updates the role of a user.

**Path Variables:**
- `id` (int): The ID of the user whose role is to be updated.

**Request Parameters:**
- `userId` (int): The ID of the user making the request.
- `role` (String): The role of the user making the request.

**Request Body:**
- `newRole` (OurUsers): The new role to be assigned to the user.

**Response:**
- `statusCode` (int): The status code of the response.
- `message` (String): A message indicating the result of the operation.