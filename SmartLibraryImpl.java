/**
 * Concrete implementation of LibraryADT.
 * All internal data structures are declared private (information hiding).
 */
public class SmartLibraryImpl implements LibraryADT {
    private BookBST catalogue;       // BST for the book catalogue
    private BorrowStack history;     // Stack for borrowing history

    public SmartLibraryImpl() {
        this.catalogue = new BookBST();
        this.history = new BorrowStack();
    }

    /**
     * Adds a book to the BST catalogue.
     */
    @Override
    public void addBook(int isbn, String title, String author) {
        boolean inserted = catalogue.insert(isbn, title, author);
        if (inserted) {
            System.out.println("\n  [SUCCESS] Book added successfully!");
            System.out.println("  ISBN: " + isbn + " | Title: " + title + " | Author: " + author);
        } else {
            System.out.println("\n  [FAILED] A book with ISBN " + isbn + " already exists in the catalogue.");
        }
    }

    /**
     * Searches for a book in the BST catalogue by ISBN using recursive BST search.
     */
    @Override
    public Book searchBook(int isbn) {
        Book found = catalogue.search(isbn);
        if (found != null) {
            System.out.println("\n  [FOUND] " + found);
        } else {
            System.out.println("\n  [NOT FOUND] No book with ISBN " + isbn + " exists in the catalogue.");
        }
        return found;
    }

    /**
     * Borrows a book: removes it from the catalogue and pushes it onto the history stack.
     */
    @Override
    public boolean borrowBook(int isbn) {
        Book found = catalogue.search(isbn);
        if (found == null) {
            System.out.println("\n  [FAILED] Cannot borrow — no book with ISBN " + isbn + " in the catalogue.");
            return false;
        }
        // Push the book record onto the history stack
        history.push(found.isbn, found.title, found.author);
        // Remove the book from the catalogue
        catalogue.delete(isbn);
        System.out.println("\n  [BORROWED] \"" + found.title + "\" has been borrowed successfully!");
        return true;
    }

    /**
     * Displays the borrowing history in LIFO order (most recent first).
     */
    @Override
    public void viewHistory() {
        System.out.println("\n  ========= Borrowing History (Most Recent First) =========");
        history.displayAll();
        System.out.println("  Total borrowed: " + history.size());
        System.out.println("  =========================================================");
    }

    /**
     * Displays all books currently in the catalogue (sorted by ISBN via in-order traversal).
     */
    public void displayCatalogue() {
        System.out.println("\n  ============ Library Catalogue (Sorted by ISBN) ============");
        catalogue.displayAll();
        System.out.println("  Total Books in Catalogue: " + getCatalogueSize());
        System.out.println("  ============================================================");
    }

    /**
     * Searches for a book by its title (O(n) traversal).
     */
    @Override
    public Book searchBookByTitle(String title) {
        Book found = catalogue.searchByTitle(title);
        if (found != null) {
            System.out.println("\n  [FOUND] " + found);
        } else {
            System.out.println("\n  [NOT FOUND] No book with title \"" + title + "\" exists in the catalogue.");
        }
        return found;
    }

    /**
     * Pops the last borrowed book from history and adds it back to the catalogue.
     */
    @Override
    public boolean returnLastBorrowedBook() {
        Book lastBorrowed = history.pop();
        if (lastBorrowed == null) {
            System.out.println("\n  [FAILED] Borrowing history is empty. Nothing to return.");
            return false;
        }
        boolean inserted = catalogue.insert(lastBorrowed.isbn, lastBorrowed.title, lastBorrowed.author);
        if (inserted) {
            System.out.println("\n  [RETURNED] \"" + lastBorrowed.title + "\" has been returned and added back to the catalogue.");
            return true;
        } else {
            System.out.println("\n  [ERROR] Failed to return book. ISBN already exists in catalogue.");
            return false;
        }
    }

    /**
     * Returns the total number of books currently in the catalogue.
     */
    @Override
    public int getCatalogueSize() {
        return catalogue.count();
    }
}
