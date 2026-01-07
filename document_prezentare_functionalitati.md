# Library Management System - Project Documentation

## 1. Business Domain Definition

The Library Management System is a Spring Boot REST API application that supports:
- **Library staff** in managing books, authors, categories, clients, and loans
- **Library clients** in browsing books, borrowing and returning them, viewing their loan history and leaving book reviews

The application exposes REST APIs and persists data in a relational database (H2/MySQL).

---

## 2. Business Requirements

**BR1 - Book Catalog Management**  
The system shall allow library staff to create, update, retrieve, and delete books in the library catalog.

**BR2 - Author Management**  
The system shall allow library staff to manage authors and associate them with one or more books.

**BR3 - Category Management**  
The system shall allow books to be organized into categories (e.g., Fiction, Science, History).

**BR4 - Book Availability Tracking**  
The system shall track how many copies of each book exist and how many are currently available for borrowing.

**BR5 - Client Management**  
The system shall allow library staff to register and manage library clients who can borrow books.

**BR6 - Book Borrowing**  
The system shall allow clients to borrow available books and automatically create a loan record.

**BR7 - Book Returning**  
The system shall allow clients to return borrowed books and automatically update book availability.

**BR8 - Loan History Tracking**  
The system shall store and expose loan history for each client and book.

**BR9 - Search and Filtering**  
The system shall allow searching and filtering of Books, Authors, Categories, and Clients by various criteria.

**BR10 - Book Reviews**  
The system shall allow clients to post reviews and ratings for books they have read.

---

## 3. MVP (Minimum Viable Product) - 5 Main Features

### Feature 1: Author Management Service
**Description:**  
Provides complete CRUD operations for managing book authors in the library system.

**Covered Business Requirements:**
- BR2 (Author Management)
- BR9 (Search and Filtering)

**Service:** `AuthorService` / `AuthorServiceImpl`

**Key Capabilities:**
- Create new authors with name and biography
- Retrieve all authors
- Retrieve author by ID
- Search authors by name
- Update author biography
- Delete authors from the system

---

### Feature 2: Book Catalog Management Service
**Description:**  
Comprehensive management of the library's book catalog including availability tracking and reviews.

**Covered Business Requirements:**
- BR1 (Book Catalog Management)
- BR4 (Book Availability Tracking)
- BR9 (Search and Filtering)
- BR10 (Book Reviews)

**Service:** `BookService` / `BookServiceImpl`

**Key Capabilities:**
- Add new books with title, description, publication date
- Link books to authors and categories
- Track total copies and available copies
- Retrieve books by ID, title, author, or category
- Update book information
- Delete books from catalog
- Manage book-category relationships (many-to-many)
- Track reviews associated with books

---

### Feature 3: Category Management Service
**Description:**  
Organizes books into logical categories for better discoverability and browsing.

**Covered Business Requirements:**
- BR3 (Category Management)
- BR9 (Search and Filtering)

**Service:** `CategoryService` / `CategoryServiceImpl`

**Key Capabilities:**
- Create new categories with name and description
- Retrieve all categories
- Retrieve category by ID
- Search categories by name
- Update category information
- Delete categories
- Manage many-to-many relationship with books

---

### Feature 4: Client Management Service
**Description:**  
Manages library client registration and information, tracking their borrowing history and reviews.

**Covered Business Requirements:**
- BR5 (Client Management)
- BR9 (Search and Filtering)

**Service:** `ClientService` / `ClientServiceImpl`

**Key Capabilities:**
- Register new library clients
- Store client details (name, email, phone, address, registration date)
- Retrieve all clients
- Retrieve client by ID
- Search clients by email or name
- Update client information
- Delete client accounts
- Link clients to their loan and review history
- Allow client to post a review for a book

---

### Feature 5: Loan Management Service
**Description:**  
Handles the complete lifecycle of book loans including borrowing, returning, and history tracking.

**Covered Business Requirements:**
- BR6 (Book Borrowing)
- BR7 (Book Returning)
- BR4 (Book Availability Tracking)
- BR8 (Loan History Tracking)
- BR9 (Search and Filtering)

**Service:** `LoanService` / `LoanServiceImpl`

**Key Capabilities:**
- Create new loan when client borrows a book
- Automatically decrease available book copies on loan creation
- Record loan date and due date
- Process book returns with return date
- Automatically increase available book copies on return
- Retrieve all active loans
- Retrieve loan history for specific clients
- Retrieve loan history for specific books
- Calculate and enforce loan periods
- Validate book availability before creating loans
- Prevent borrowing when no copies available

