/**
 * Defines the core operations that any library implementation must provide.
 * This guarantees information hiding: users interact only through this contract.
 */
public interface LibraryADT {
    void addBook(int isbn, String title, String author);
    Book searchBook(int isbn);
    boolean borrowBook(int isbn);
    void viewHistory();
    Book searchBookByTitle(String title);
    boolean returnLastBorrowedBook();
    int getCatalogueSize();
}
