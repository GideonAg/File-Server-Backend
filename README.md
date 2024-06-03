# FILE SERVER APPLICATION

## [Backend hosted here on Azure]()

* [LOGIN](#login)
* [REGISTER](#user-registration)
* [EMAIL VERIFICATION](#email-verification)
* [UPDATE PASSWORD](#update-password)
* [FORGOT PASSWORD](#forgot-password)
* [FILE CONTROLLER ENDPOINTS](#file-controller-endpoints)
* [UPLOAD FILES](#upload-files)
* [ADMIN GET ALL FILES](#admin-get-all-files)
* [ADMIN SEARCH FOR FILES](#admin-search-for-files)
* [ADMIN DELETE FILE](#admin-delete-file)
* [USER GET ALL FILES](#user-get-all-files)
* [USER SEARCH FOR FILE](#user-search-for-file)
* [DOWNLOAD FILE](#download-file)
* [SEND FILE TO AN EMAIL](#send-file-to-an-email)


## LOGIN

### Request Information

| Method | URL                                                      |
|--------|----------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/auth/login |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| email         | String | true     |
| password      | String | true     |

### Error Responses

| Code | Message                            |
|------|------------------------------------|
| 403  | "Bad Credentials"                  |
| 403  | "User is disabled"                 |
| 400  | "Invalid email address format"     |
| 400  | "Email or password can't be blank" |
| 403  | "User Not Found"                   |

### Successful Response Example

```
{
    "userEmail": user@gmail.com,
    "userId": "userIdHere",
    "jwt": "token"
}
```

## USER REGISTRATION

### Request Information

| Method | URL                                                         |
|--------|-------------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/auth/register |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| email         | String | true     |
| password      | String | true     |

### Error Responses

| Code | Message                            |
|------|------------------------------------|
| 406  | "User is already registered        |
| 400  | "Invalid email address format"     |
| 400  | "Email or password can't be blank" |
### Successful Response Example

```
  "Account verification link sent to email."
```

## EMAIL VERIFICATION

### Request Information

| Method | URL                                                                |
|--------|--------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/auth/register/verify |

### Request Param

| Request Param Name | type   | required |
|--------------------|--------|----------|
| token              | String | true     |

### Successful Response Example

| Code | Message                           |
|------|-----------------------------------|
| 200  | "Account verification successful" |
| 200  | "Account already verified"        |


## UPDATE PASSWORD

### Request Information

| Method | URL                                                                |
|--------|--------------------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/auth/update-password |

### Header

| Type         | Property Name    |
|--------------|------------------|
| Allow        | POST, OPTION     |
| Content-Type | Application/Json |

### Request Param
| Property Name | type   | required |
|---------------|--------|----------|
| token         | String | true     |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| password      | String | true     |

### Error Responses

| Code | Message                                                               |
|------|-----------------------------------------------------------------------|
| 400  | "Password should contain numbers and digits only. 3 to 20 characters" |
| 404  | "Password update failed"                                              |
| 400  | "Password is required"                                                |

### Successful Response Example

```
    Password updated successfully
```

## FORGOT PASSWORD

### Request Information

| Method | URL                                                                |
|--------|--------------------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/auth/forgot-password |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| email         | String | true     |

### Error Responses

| Code | Message                          |
|------|----------------------------------|
| 400  | "Please enter a valid email"     |
| 400  | "Email cannot be null or empty"  |
| 404  | "Incorrect email address"        |

### Successful Response Example

```
    Password reset link sent to email
```


# FILE CONTROLLER ENDPOINTS

## UPLOAD FILES

### Request Information

| Method | URL                                                       |
|--------|-----------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/file/upload |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| title         | String | true     |
| description   | String | true     |
| file          | file   | true     |

### Error Responses

| Code | Message                                             |
|------|-----------------------------------------------------| 
| 403  | "You do not have permission to perform this action" |
| 400  | "Title, description and file are required"          |

### Successful Response Example

```
    File uploaded successfully
```

## ADMIN GET ALL FILES

### Request Information

| Method | URL                                                                |
|--------|--------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/admin/all-files |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |


### Error Responses

| Code | Message     |
|------|-------------| 
| 403  | "Forbidden" |

### Successful Response Example

```
[
  {
    "id": "6ccbcaa2-9194-420f-8b68-ce4ce3e651a2",
    "title": "gdg",
    "description": "d",
    "file": null,
    "numberOfDownloads": 3,
    "numberOfShares": 0,
    "fileType": null
  },
  {
    "id": "52527a09-8062-41aa-820b-36b02bf0e91b",
    "title": "Devs",
    "description": "compsa",
    "file": null,
    "numberOfDownloads": 8,
    "numberOfShares": 0,
    "fileType": null
  }
]
```

## ADMIN SEARCH FOR FILES

### Request Information

| Method | URL                                                                                 |
|--------|-------------------------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/admin/search-for-file/{fileName} |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |


### Error Responses

| Code | Message     |
|------|-------------| 
| 403  | "Forbidden" |

### Successful Response Example
```
[
  {
    "id": "d79c6f74-d88d-4804-9cd9-af63daeaae87",
    "title": "ppt",
    "description": "ppt des",
    "file": null,
    "numberOfDownloads": 1,
    "numberOfShares": 0,
    "fileType": null
  }
]
```

## ADMIN DELETE FILE

### Request Information

| Method | URL                                                                |
|--------|--------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/delete/{fileId} |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |


### Error Responses

| Code | Message                 |
|------|-------------------------| 
| 403  | "Forbidden"             |
| 404  | "Failed to delete file" |

### Successful Response Example
```
    File deleted successfully
```

## USER GET ALL FILES

### Request Information

| Method | URL                                                               |
|--------|-------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/user/all-files |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |


### Error Responses

| Code | Message     |
|------|-------------| 
| 403  | "Forbidden" |

### Successful Response Example
```
[
  {
    "id": "6ccbcaa2-9194-420f-8b68-ce4ce3e651a2",
    "title": "gdg",
    "description": "d"
  },
  {
    "id": "52527a09-8062-41aa-820b-36b02bf0e91b",
    "title": "Devs",
    "description": "compsa"
  }
]
```

## USER SEARCH FOR FILE

### Request Information

| Method | URL                                                                                |
|--------|------------------------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/user/search-for-file/{fileName} |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |


### Error Responses

| Code | Message     |
|------|-------------| 
| 403  | "Forbidden" |


### Successful Response Example

```
[
  {
    "id": "d79c6f74-d88d-4804-9cd9-af63daeaae87",
    "title": "ppt",
    "description": "ppt des",
    "file": null,
    "numberOfDownloads": 0,
    "numberOfShares": 0,
    "fileType": null
  }
]
```

## DOWNLOAD FILE

### Request Information

| Method | URL                                                              |
|--------|------------------------------------------------------------------|
| GET    | https://file-server-backend-1n25.onrender.com/file/download/{id} |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| id            | String | true     |

### Error Responses

| Code | Message          |
|------|------------------| 
| 404  | "File Not Found" |

### Successful Response Example
```
returns a byte array of the file
```

## SEND FILE TO AN EMAIL

### Request Information

| Method | URL                                                      |
|--------|----------------------------------------------------------|
| POST   | https://file-server-backend-1n25.onrender.com/file/share |

### Header

| Type         | Property Name        |
|--------------|----------------------|
| Allow        | POST, OPTION         |
| Content-Type | Application/Json     |
| token        | Authentication Token |

### JSON Body

| Property Name | type   | required |
|---------------|--------|----------|
| receiverEmail | String | true     |
| fileId        | String | true     |

### Error Responses

| Code | Message                                   |
|------|-------------------------------------------|
| 400  | "Receiver email and file id are required" |
| 400  | "Invalid email address format "           |
| 404  | "File not found"                          |

### Successful Response Example
```
File sent to {receiverEmail} successfully
```