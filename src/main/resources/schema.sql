DROP TABLE IF EXISTS Authors;

CREATE TABLE Authors (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Biography VARCHAR(511),
    CONSTRAINT UniqueAuthorName UNIQUE (Name)
);

DROP TABLE IF EXISTS Categories;

CREATE TABLE Categories (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description VARCHAR(511),
    CONSTRAINT UniqueCategoryName UNIQUE (Name)
);

DROP TABLE IF EXISTS Books;

CREATE TABLE Books (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    Description VARCHAR(511),
    publication_date TIMESTAMP,
    author_id BIGINT NOT NULL,
    total_copies INT NOT NULL CHECK (total_copies > 0),
    available_copies INT NOT NULL CHECK (available_copies > 0),
    CONSTRAINT UniqueBookTitle UNIQUE (Title),
    CONSTRAINT FkBooksAuthors FOREIGN KEY (author_id) REFERENCES Authors(Id) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS Book_Categories;

CREATE TABLE Book_Categories (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT UniqueBookCategory UNIQUE (book_id, category_id),
    CONSTRAINT FkBookCategoriesBooks FOREIGN KEY (book_id) REFERENCES Books(Id) ON DELETE CASCADE,
    CONSTRAINT FkBookCategoriesCategories FOREIGN KEY (category_id) REFERENCES Categories(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Clients;

CREATE TABLE Clients (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UniquePhone UNIQUE (Phone)
);

DROP TABLE IF EXISTS Loans;

CREATE TABLE Loans (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    CONSTRAINT UniqueBookClientLoan UNIQUE (book_id, client_id),
    CONSTRAINT FkLoansBooks FOREIGN KEY (book_id) REFERENCES Books(Id) ON DELETE CASCADE,
    CONSTRAINT FkLoansClients FOREIGN KEY (client_id) REFERENCES Clients(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Reviews;

CREATE TABLE Reviews (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    review_text VARCHAR(511),
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UniqueClientBookReview UNIQUE (client_id, book_id),
    CONSTRAINT FkReviewsBooks FOREIGN KEY (book_id) REFERENCES Books(Id) ON DELETE CASCADE,
    CONSTRAINT FkReviewsClients FOREIGN KEY (client_id) REFERENCES Clients(Id) ON DELETE CASCADE
);

-- Insert Authors
INSERT INTO Authors (Name, Biography) VALUES
('George R. R. Martin', 'American novelist known for the A Song of Ice and Fire series'),
('J.K. Rowling', 'British author famous for the Harry Potter series'),
('Isaac Asimov', 'American science fiction author and professor of biochemistry'),
('Arthur Conan Doyle', 'British writer and physician, creator of Sherlock Holmes'),
('Stephen King', 'American author of horror and supernatural fiction');

-- Insert Categories
INSERT INTO Categories (Name, Description) VALUES
('Fantasy', 'Fantasy and magical worlds'),
('Science Fiction', 'Science fiction and futuristic settings'),
('Mystery', 'Detective and mystery novels'),
('Horror', 'Horror and suspense novels'),
('Young Adult', 'Books aimed at young adult readers');

-- Insert Books
INSERT INTO Books (Title, Description, publication_date, author_id, total_copies, available_copies) VALUES
('A Game of Thrones', 'The first book in A Song of Ice and Fire series', '1996-08-06', 1, 5, 5),
('A Clash of Kings', 'The second book in A Song of Ice and Fire series', '1998-11-16', 1, 4, 4),
('A Storm of Swords', 'The third book in A Song of Ice and Fire series', '2000-08-08', 1, 3, 3),
('Harry Potter and the Philosopher''s Stone', 'The beginning of Harry''s magical journey', '1997-06-26', 2, 10, 10),
('Harry Potter and the Chamber of Secrets', 'Harry''s second year at Hogwarts', '1998-07-02', 2, 8, 8),
('Harry Potter and the Prisoner of Azkaban', 'Harry''s third year at Hogwarts', '1999-07-08', 2, 7, 7),
('Foundation', 'The first novel in Isaac Asimov''s Foundation series', '1951-06-01', 3, 3, 3),
('I, Robot', 'A collection of short stories about robots', '1950-12-02', 3, 4, 4),
('A Study in Scarlet', 'The first Sherlock Holmes novel', '1887-11-01', 4, 2, 2),
('The Shining', 'A horror novel set in an isolated hotel', '1977-01-28', 5, 3, 3),
('Carrie', 'A horror novel about telekinetic powers', '1974-04-05', 5, 5, 5),
('The Stand', 'An epic post-apocalyptic horror novel', '1978-10-03', 5, 4, 4);

-- Insert Book-Category Relationships
INSERT INTO Book_Categories (book_id, category_id) VALUES
(1, 1),  -- A Game of Thrones - Fantasy
(2, 1),  -- A Clash of Kings - Fantasy
(3, 1),  -- A Storm of Swords - Fantasy
(4, 1),  -- Harry Potter 1 - Fantasy
(4, 5),  -- Harry Potter 1 - Young Adult
(5, 1),  -- Harry Potter 2 - Fantasy
(5, 5),  -- Harry Potter 2 - Young Adult
(6, 1),  -- Harry Potter 3 - Fantasy
(6, 5),  -- Harry Potter 3 - Young Adult
(7, 2),  -- Foundation - Science Fiction
(8, 2),  -- I, Robot - Science Fiction
(9, 3),  -- A Study in Scarlet - Mystery
(10, 4), -- The Shining - Horror
(11, 4), -- Carrie - Horror
(12, 4); -- The Stand - Horror

-- Insert Clients
INSERT INTO Clients (Name, Phone) VALUES
('Alice Johnson', '555-0001'),
('Bob Smith', '555-0002'),
('Carol White', '555-0003'),
('Diana Brown', '555-0004'),
('Edward Davis', '555-0005');

-- Insert Loans (some active, some returned, simulating history)
INSERT INTO Loans (book_id, client_id, borrow_date, due_date, return_date) VALUES
-- Active loans
(1, 1, '2026-01-02 10:00:00', '2026-01-16', NULL),
(4, 2, '2026-01-03 14:30:00', '2026-01-17', NULL),
(7, 3, '2026-01-04 09:15:00', '2026-01-18', NULL),

-- Returned loans
(1, 2, '2025-12-20 11:00:00', '2026-01-03', '2026-01-02'),
(4, 1, '2025-12-15 15:45:00', '2025-12-29', '2025-12-28'),
(8, 4, '2025-12-10 10:30:00', '2025-12-24', '2025-12-23'),
(9, 5, '2025-12-05 13:20:00', '2025-12-19', '2025-12-18'),

-- Overdue returned loans
(10, 1, '2025-11-30 16:00:00', '2025-12-14', '2025-12-19'),
(11, 3, '2025-11-25 10:45:00', '2025-12-09', '2025-12-29');

-- Update Book Availability for books with active loans (return_date IS NULL)
UPDATE Books SET available_copies = available_copies - 1 WHERE Id = 1;  -- A Game of Thrones
UPDATE Books SET available_copies = available_copies - 1 WHERE Id = 4;  -- Harry Potter 1
UPDATE Books SET available_copies = available_copies - 1 WHERE Id = 7;  -- Foundation

-- Insert Reviews
INSERT INTO Reviews (book_id, client_id, review_text) VALUES
(1, 1, 'An epic fantasy masterpiece! Could not put it down.'),
(1, 2, 'Great story with complex characters. A bit slow in places.'),
(4, 1, 'A magical adventure that captivated me from start to finish!'),
(4, 2, 'The perfect introduction to the wizarding world.'),
(7, 3, 'Fascinating science fiction concept. Classic foundation for the genre.'),
(8, 4, 'Innovative robot stories that still feel relevant today.'),
(9, 5, 'The quintessential detective novel. Sherlock is brilliant!'),
(10, 1, 'Terrifying and unforgettable. A must-read horror masterpiece.'),
(11, 3, 'Creepy and engaging. The telekinetic powers are captivating.'),
(12, 4, 'Epic post-apocalyptic tale. One of King''s best works.');
