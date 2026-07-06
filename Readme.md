Spring Boot setup (1 hr)

PostgreSQL configuration (1 hr)

Product, Category, Supplier entities (2 hrs)

CRUD APIs (3 hrs)

Testing with Postman (1–2 hrs)



Day 2 (8–10 hrs): Business Logic



JWT authentication (3 hrs)

Inventory operations (2 hrs)

Validation \& exception handling (1 hr)

Pagination, sorting, filtering (2 hrs)



Day 3 (8–10 hrs): Polish



Swagger/OpenAPI (1 hr)

Docker (2 hrs)

Unit tests (2 hrs)

README with screenshots (2 hrs)

Deployment (Render/Railway if possible) (2–3 hr)





Notes:

\------------------------------------------------------------------------------------------------

Refactor to Data Transfer Objects

\------------------------------------------------------------------------------------------------

Abstract Resource not found exception

\------------------------------------------------------------------------------------------------

Validation



Your DTO should eventually contain validation.



Instead of



private String name;



you'll have



@NotBlank

private String name;

@Positive

private double price;

@Min(0)

private int quantity;



Then in the controller:



public Product addProduct(@Valid @RequestBody ProductRequest request)



Spring will automatically reject invalid requests before they reach the service.



5\. Duplicate SKU



Right now someone can do



Laptop

SKU001



Phone

SKU001



Your database will throw an exception because sku is unique.



Instead, you'd eventually check:



if(productRepository.existsBySku(request.getSku())) {

&#x20;   throw new DuplicateSkuException(request.getSku());

}



That's more user-friendly than relying on a database constraint violation.

\------------------------------------------------------------------------------------------------

✅ Product DTOs (done)

➜ JWT Authentication

➜ Inventory operations (add/remove stock)

➜ Validation (@Valid, @NotBlank, etc.)

➜ Swagger/OpenAPI

➜ Docker

➜ Unit tests

➜ If there's still time, refactor Category and Supplier to use DTOs too.

\------------------------------------------------------------------------------------------------
Security Management



| Action                 | EMPLOYEE | MANAGER | ADMIN |

| ---------------------- | -------- | ------- | ----- |

| View Products          | ✅        | ✅       | ✅     |

| Add Products           | ❌        | ✅       | ✅     |

| Update Products        | ❌        | ✅       | ✅     |

| Delete Products        | ❌        | ❌       | ✅     |

| Manage Categories      | ❌        | ✅       | ✅     |

| Manage Suppliers       | ❌        | ✅       | ✅     |

| Register Employees     | ❌        | ❌       | ✅     |

| Create Managers/Admins | ❌        | ❌       | ✅     |



\------------------------------------------------------------------------------------------------



This has to be implemented in the user service

Register user

Update user

Delete user

Find user



\------------------------------------------------------------------------------------------------

This has to be implemented in the Authentication Service

login()



generateToken()



refreshToken()



validateToken()



logout()



revokeToken()   // if you implement token blacklisting

\------------------------------------------------------------------------------------------------

Our Roadmap

Phase 1



✅ JwtService



Phase 2



Modify login.



Instead of



{

&#x20;   "message":"Login Successful"

}



Return



{

&#x20;   "token":"eyJhbGc..."

}

Phase 3



JWT Filter



Bearer Token



↓



Validate



↓



Set Authentication



↓



Controller

Phase 4



Secure everything.



ADMIN



↓



DELETE Product



✓

EMPLOYEE



↓



DELETE Product



403 Forbidden

Wrap JWT parsing in a try-catch



Right now you do



String username = jwtService.extractUsername(jwt);



Imagine somebody sends



Authorization: Bearer hello



or



Bearer abc123



extractUsername() will throw an exception and your application returns 500 Internal Server Error.



Instead:



try {



&#x20;   String username = jwtService.extractUsername(jwt);



&#x20;   ...



} catch (Exception e) {



&#x20;   filterChain.doFilter(request, response);

&#x20;   return;



}



Later we'll make this more specific (ExpiredJwtException, MalformedJwtException, etc.), but for now this is enough.

------------------------------------------------------------------------------------------------

One more improvement



Instead of



catch (Exception e)



I'd eventually make it:



catch (JwtException | IllegalArgumentException e) {

&#x20;   // Invalid token

}

\------------------------------------------------------------------------------------------------

Here's the order I'd follow:



✅ CRUD

✅ JWT Authentication

➡️ Role-Based Authorization (RBAC) (next)

Validation improvements (@Valid, custom validation messages)

Pagination and sorting

Search and filtering

Audit fields (createdAt, updatedAt)

Swagger/OpenAPI documentation

Unit tests (JUnit + Mockito)

Docker

Docker Compose (PostgreSQL + app)

GitHub Actions CI

