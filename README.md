# Smart Library Management System

A Java console application built as a Data Structures course project (WIA1002). The system manages a library's book catalogue using a **Binary Search Tree (BST)** and tracks borrowing history using a **Stack (linked-list based)**.

**Group 6 — OCC3**
-Raye Chan Jun Foong (25006003)
-Yip Zheng Xyun
-Liew Jicson
-Ong Zheng Xi
-Hon Chi Fung

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Core Functions](#core-functions)
- [Additional Functions](#additional-functions)
- [Data Structures Used](#data-structures-used)
- [Class Descriptions](#class-descriptions)

---

## Project Overview

The Smart Library Management System allows users to:

- **Add** books to a catalogue (stored in a BST indexed by ISBN)
- **Search** for books by ISBN or by title
- **Borrow** books (removes from catalogue, records in history stack)
- **Return** the last borrowed book (pops from history, re-adds to catalogue)
- **View** the full catalogue and borrowing history

The system demonstrates the practical use of two fundamental data structures — a Binary Search Tree for efficient lookups and a Stack for LIFO borrowing history — connected through a clean interface-driven architecture.

---

## Architecture

The project follows a layered design with information hiding enforced through an interface:

```
┌─────────────────────────────────────────────┐
│  SmartLibrary.java          (UI Layer)      │
│  - Menu display, input validation, Scanner  │
└──────────────────┬──────────────────────────┘
                   │ uses
                   ▼
┌─────────────────────────────────────────────┐
│  SmartLibraryImpl.java    (Business Logic)  │
│  - Implements LibraryADT interface          │
│  - Orchestrates BST and Stack operations    │
└────────┬───────────────────────┬────────────┘
         │                       │
         ▼                       ▼
┌─────────────────┐   ┌──────────────────────┐
│  BookBST.java   │   │  BorrowStack.java    │
│  (BST for       │   │  (Linked Stack for   │
│   catalogue)    │   │   borrow history)    │
│       │         │   │        │             │
│       ▼         │   │        ▼             │
│  Book.java      │   │  StackNode (inner)   │
│  (BST node +    │   │                      │
│   data model)   │   │                      │
└─────────────────┘   └──────────────────────┘
```

- **`LibraryADT`** — The interface contract. All public operations are declared here.
- **`SmartLibrary`** — The UI layer. Handles all user interaction and input validation.
- **`SmartLibraryImpl`** — The business logic layer. Implements `LibraryADT` and coordinates the two data structures.
- **`BookBST`** — A Binary Search Tree that stores `Book` nodes indexed by ISBN.
- **`BorrowStack`** — A LIFO stack (singly linked list) that stores borrowed book records.
- **`Book`** — A dual-purpose class: data model for a book AND the node structure for the BST.

---

## How to Run

### Prerequisites

- **Java Development Kit (JDK) 8** or higher installed
- A terminal or command prompt

### Compile

Navigate to the project directory and compile all source files:

```bash
javac SmartLibrary.java
```

This will automatically compile all dependent classes (`Book.java`, `BookBST.java`, `BorrowStack.java`, `LibraryADT.java`, `SmartLibraryImpl.java`).

### Run

```bash
java SmartLibrary
```

### Sample Session

```
=============================================
     SMART LIBRARY MANAGEMENT SYSTEM
=============================================

=============================================
  MAIN MENU
=============================================
  1. Add Book
  2. Search Book (by ISBN)
  3. Search Book (by Title)
  4. Borrow Book
  5. Return Last Borrowed Book (Undo)
  6. View Borrowing History
  7. Display Catalogue
  8. Exit
=============================================
  Enter your choice (1-8): 1

  --- Add a New Book ---
  Enter ISBN (positive integer): 101
  Enter Title: Data Structures and Algorithms
  Enter Author: John Smith

  [SUCCESS] Book added successfully!
  ISBN: 101 | Title: Data Structures and Algorithms | Author: John Smith
```

---

## Project Structure

```
WIA1002_DS_OCC3_GROUP6/
├── LibraryADT.java          # Interface defining the library contract (7 methods)
├── Book.java                # Data model + BST node (isbn, title, author, left, right)
├── BookBST.java             # Binary Search Tree for the book catalogue
├── BorrowStack.java         # Linked-list stack for borrowing history
├── SmartLibraryImpl.java    # Business logic implementing LibraryADT
├── SmartLibrary.java        # Main entry point with menu-driven UI
├── class/                   # Compiled .class files (output directory)
└── README.md                # This file
```

---

## Core Functions

These are the fundamental operations that form the base library system.

### 1. Add Book

**Menu Option:** 1  
**Flow:** `SmartLibrary.addBookMenu()` → `SmartLibraryImpl.addBook()` → `BookBST.insert()`

Adds a new book to the catalogue. The book is inserted into the BST at the correct position based on its ISBN.

**How it works:**
1. The user enters an ISBN, title, and author through the menu.
2. Input is validated — ISBN must be a positive integer, title and author cannot be empty.
3. `BookBST.insert()` first checks if a book with that ISBN already exists (via `search()`). If it does, the insertion is rejected.
4. If the ISBN is unique, `insertRecursive()` traverses the BST from the root, going left if the new ISBN is less than the current node's ISBN, or right if greater, until it finds a null position to place the new `Book` node.

**Time Complexity:** O(log n) average case, O(n) worst case (degenerate tree)

---

### 2. Search Book by ISBN

**Menu Option:** 2  
**Flow:** `SmartLibrary.searchBookMenu()` → `SmartLibraryImpl.searchBook()` → `BookBST.search()`

Searches for a book in the catalogue using its ISBN.

**How it works:**
1. The user enters an ISBN.
2. `BookBST.search()` calls `searchRecursive()`, which starts at the root of the BST.
3. At each node, it compares the target ISBN with the current node's ISBN:
   - If equal → the book is found, return it.
   - If less → recurse into the left subtree.
   - If greater → recurse into the right subtree.
4. If the recursion reaches a null node, the book does not exist.

**Time Complexity:** O(log n) average case, O(n) worst case

---

### 3. Borrow Book

**Menu Option:** 4  
**Flow:** `SmartLibrary.borrowBookMenu()` → `SmartLibraryImpl.borrowBook()` → `BookBST.search()` + `BorrowStack.push()` + `BookBST.delete()`

Borrows a book by removing it from the catalogue and recording it in the borrowing history.

**How it works:**
1. The user enters the ISBN of the book to borrow.
2. `SmartLibraryImpl.borrowBook()` first searches the BST for the book.
3. If found, the book's details (ISBN, title, author) are pushed onto the `BorrowStack`.
4. The book is then deleted from the `BookBST` using `delete()`.
5. BST deletion handles three cases:
   - **Leaf node:** Simply remove it (return null to parent).
   - **One child:** Replace the node with its only child.
   - **Two children:** Find the in-order successor (smallest node in the right subtree), copy its data into the current node, then delete the successor.

**Time Complexity:** O(log n) average case for search + delete

---

### 4. View Borrowing History

**Menu Option:** 6  
**Flow:** `SmartLibrary` → `SmartLibraryImpl.viewHistory()` → `BorrowStack.displayAll()`

Displays all borrowed books in reverse chronological order (most recent first).

**How it works:**
1. `BorrowStack.displayAll()` starts at the `top` of the stack.
2. It traverses the singly linked list from top to bottom, printing each `StackNode`'s details.
3. Since the stack is LIFO (Last In, First Out), the most recently borrowed book appears first.
4. The total number of borrowed books is also displayed.

**Time Complexity:** O(n) where n is the number of borrowed books

---

### 5. Display Catalogue

**Menu Option:** 7  
**Flow:** `SmartLibrary` → `SmartLibraryImpl.displayCatalogue()` → `BookBST.displayAll()`

Displays all books currently available in the catalogue, sorted by ISBN.

**How it works:**
1. `BookBST.displayAll()` performs an **in-order traversal** of the BST (left → root → right).
2. In-order traversal of a BST visits nodes in ascending order of their keys, so books are printed sorted by ISBN.
3. The total number of books in the catalogue is displayed at the end.

**Time Complexity:** O(n)

---

### 6. Exit

**Menu Option:** 8

Terminates the program gracefully, closing the Scanner and printing a goodbye message.

---

## Additional Functions

These functions were added on top of the core system to enhance functionality.

### 1. Search Book by Title

**Menu Option:** 3  
**Flow:** `SmartLibrary.searchBookByTitleMenu()` → `SmartLibraryImpl.searchBookByTitle()` → `BookBST.searchByTitle()`

Searches for a book by its title instead of ISBN. This is an additional function because the BST is indexed by ISBN, not by title, so a title search requires a different approach.

**How it works:**
1. The user enters a book title.
2. `BookBST.searchByTitle()` calls `searchByTitleRecursive()`, which traverses the **entire BST** (not just one branch).
3. At each node, it compares the target title with the current node's title using `equalsIgnoreCase()` for case-insensitive matching.
4. It searches the left subtree first; if not found, it searches the right subtree.
5. Returns the first match found.

**Time Complexity:** O(n) — must check every node since the BST is not ordered by title.

**Why it's additional:** The BST is indexed by ISBN, so searching by title cannot take advantage of the tree's ordering. This requires a full traversal, which is a fundamentally different operation from the core ISBN-based search.

---

### 2. Return Last Borrowed Book (Undo)

**Menu Option:** 5  
**Flow:** `SmartLibrary` → `SmartLibraryImpl.returnLastBorrowedBook()` → `BorrowStack.pop()` + `BookBST.insert()`

Returns the most recently borrowed book back to the catalogue. This acts as an "undo" for the last borrow operation.

**How it works:**
1. `SmartLibraryImpl.returnLastBorrowedBook()` calls `BorrowStack.pop()`.
2. `pop()` removes the top `StackNode` from the linked list, creates a new `Book` object with its details, and returns it.
3. If the stack is empty (no borrowed books), the operation fails with a message.
4. If a book is popped, it is re-inserted into the BST catalogue using `BookBST.insert()`.
5. Since the book was previously deleted from the BST when borrowed, the ISBN should be unique and insertion should succeed.

**Time Complexity:** O(1) for pop + O(log n) for insert

**Why it's additional:** The stack data structure naturally supports an "undo" pattern — the last item pushed is the first to be popped. This leverages the LIFO property to implement a return feature without needing to track which specific book the user wants to return.

---

### 3. Count Books in Catalogue

**Menu Option:** Displayed as part of option 7 (Display Catalogue)  
**Flow:** `SmartLibraryImpl.displayCatalogue()` → `SmartLibraryImpl.getCatalogueSize()` → `BookBST.count()`

Counts and displays the total number of books currently in the catalogue.

**How it works:**
1. `BookBST.count()` calls `countRecursive()` starting from the root.
2. `countRecursive()` returns `0` for a null node, otherwise returns `1 + countRecursive(left) + countRecursive(right)`.
3. This recursively counts every node in the BST.
4. The count is displayed at the bottom of the catalogue view.

**Time Complexity:** O(n)

**Why it's additional:** While the BST already tracks books, there was no built-in way to get the total count. This recursive counter provides a summary statistic shown alongside the catalogue display.

---

## Data Structures Used

### Binary Search Tree (BST)

- **Purpose:** Stores the book catalogue, indexed by ISBN.
- **Implementation:** Linked nodes (`Book` objects) with `left` and `right` child pointers.
- **Key Property:** For any node, all nodes in the left subtree have smaller ISBNs, and all nodes in the right subtree have larger ISBNs.
- **Operations:**
  | Operation | Method | Average Time | Worst Case |
  |-----------|--------|-------------|------------|
  | Insert | `insert()` | O(log n) | O(n) |
  | Search | `search()` | O(log n) | O(n) |
  | Delete | `delete()` | O(log n) | O(n) |
  | Display All | `displayAll()` | O(n) | O(n) |
  | Search by Title | `searchByTitle()` | O(n) | O(n) |
  | Count | `count()` | O(n) | O(n) |

### Stack (Linked List)

- **Purpose:** Tracks borrowing history in LIFO order.
- **Implementation:** Singly linked list of `StackNode` objects with a `top` pointer.
- **Key Property:** Last In, First Out — the most recently borrowed book is always on top.
- **Operations:**
  | Operation | Method | Time Complexity |
  |-----------|--------|----------------|
  | Push | `push()` | O(1) |
  | Pop | `pop()` | O(1) |
  | Is Empty | `isEmpty()` | O(1) |
  | Size | `size()` | O(1) |
  | Display All | `displayAll()` | O(n) |

---

## Class Descriptions

### `LibraryADT.java`

The **interface** that defines the public contract for the library system. Any implementation must provide these 7 methods:

```java
void addBook(int isbn, String title, String author);
Book searchBook(int isbn);
boolean borrowBook(int isbn);
void viewHistory();
Book searchBookByTitle(String title);
boolean returnLastBorrowedBook();
int getCatalogueSize();
```

This enforces **information hiding** — the UI layer (`SmartLibrary`) only interacts through this interface, not through the internal data structures directly.

---

### `Book.java`

A **dual-purpose class** that serves as both:

1. **Data model** — Holds book information: `isbn` (int), `title` (String), `author` (String).
2. **BST node** — Contains `left` and `right` child pointers for the Binary Search Tree.

Includes a `toString()` override for formatted display:
```
ISBN: 101 | Title: Data Structures | Author: John Smith
```

---

### `BookBST.java`

Implements the **Binary Search Tree** for the book catalogue. Contains 6 public methods and their private recursive helpers:

| Method | Description |
|--------|-------------|
| `insert(isbn, title, author)` | Adds a book; rejects duplicate ISBNs |
| `search(isbn)` | Finds a book by ISBN |
| `delete(isbn)` | Removes a book (handles all 3 BST deletion cases) |
| `displayAll()` | Prints all books sorted by ISBN (in-order traversal) |
| `searchByTitle(title)` | Finds a book by title (case-insensitive, O(n)) |
| `count()` | Returns total number of books |

**BST Deletion Cases:**
1. **Leaf node** — No children; simply remove.
2. **One child** — Replace node with its child.
3. **Two children** — Find in-order successor (min of right subtree), copy data, delete successor.

---

### `BorrowStack.java`

Implements a **LIFO stack** using a singly linked list. Uses a private inner class `StackNode` for the linked list nodes.

| Method | Description |
|--------|-------------|
| `push(isbn, title, author)` | Adds a borrowed book record to the top |
| `pop()` | Removes and returns the most recent borrowed book |
| `isEmpty()` | Checks if the stack has any records |
| `size()` | Returns the number of records |
| `displayAll()` | Prints all records from top to bottom |

---

### `SmartLibraryImpl.java`

The **business logic layer** that implements `LibraryADT`. Holds two private fields:

- `BookBST catalogue` — The book catalogue
- `BorrowStack history` — The borrowing history

This class is the **facade** that connects the two data structures. The key operation is `borrowBook()`, which:
1. Searches the BST for the book
2. Pushes it onto the stack
3. Deletes it from the BST

And `returnLastBorrowedBook()`, which reverses the process:
1. Pops from the stack
2. Inserts back into the BST

---

### `SmartLibrary.java`

The **main entry point** with a menu-driven console interface. Features:

- 8-option menu with a `while(running)` main loop
- Input validation using `try-catch` for `NumberFormatException`
- Empty-string checks for title and author inputs
- Private static helper methods for each menu option (`addBookMenu`, `searchBookMenu`, `searchBookByTitleMenu`, `borrowBookMenu`)

---

## Menu Options Summary

| Option | Function | Type | Description |
|--------|----------|------|-------------|
| 1 | Add Book | Core | Insert a new book into the BST catalogue |
| 2 | Search by ISBN | Core | Find a book using its ISBN (O(log n)) |
| 3 | Search by Title | Additional | Find a book using its title (O(n)) |
| 4 | Borrow Book | Core | Remove from catalogue, record in history |
| 5 | Return Last Book | Additional | Undo last borrow (pop from stack, re-insert) |
| 6 | View History | Core | Display all borrowed books (LIFO order) |
| 7 | Display Catalogue | Core + Additional | Show all books sorted by ISBN + total count |
| 8 | Exit | Core | Terminate the program |
