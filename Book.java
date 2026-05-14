/**
 * Represents a single book in the library.
 * Also serves as a node in the Binary Search Tree (left/right pointers).
 */
public class Book {
    int isbn;
    String title;
    String author;
    Book left;   // left child for BST
    Book right;  // right child for BST

    public Book(int isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + " | Title: " + title + " | Author: " + author;
    }
}
