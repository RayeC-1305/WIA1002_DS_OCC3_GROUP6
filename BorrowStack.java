/**
 * A stack (LIFO) data structure to track borrowed books.
 * The most recently borrowed book is always on top.
 */
public class BorrowStack {

    /**
     * Internal node for the linked-list-based stack.
     */
    private class StackNode {
        int isbn;
        String title;
        String author;
        StackNode next;

        StackNode(int isbn, String title, String author) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.next = null;
        }
    }

    private StackNode top;
    private int size;

    public BorrowStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes a borrowed book record onto the stack.
     */
    public void push(int isbn, String title, String author) {
        StackNode newNode = new StackNode(isbn, title, author);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Checks whether the stack is empty.
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns the number of records in the stack.
     */
    public int size() {
        return size;
    }

    /**
     * Displays all borrowed books in LIFO order (most recent first).
     */
    public void displayAll() {
        if (isEmpty()) {
            System.out.println("  (No borrowing history)");
            return;
        }
        int index = 1;
        StackNode current = top;
        while (current != null) {
            System.out.println("  " + index + ". ISBN: " + current.isbn
                    + " | Title: " + current.title
                    + " | Author: " + current.author);
            current = current.next;
            index++;
        }
    }
}
