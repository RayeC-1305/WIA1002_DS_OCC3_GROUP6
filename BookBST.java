/**
 * Stores the library's book catalogue in a Binary Search Tree,
 * indexed by ISBN for O(log n) average-case search.
 */
public class BookBST {
    private Book root;

    public BookBST() {
        this.root = null;
    }

    // --- Insert -----------------------------------------------------------
    /**
     * Public method to insert a new book into the BST.
     * @return true if the book was inserted, false if the ISBN already exists.
     */
    public boolean insert(int isbn, String title, String author) {
        if (search(isbn) != null) {
            return false; // duplicate ISBN
        }
        root = insertRecursive(root, isbn, title, author);
        return true;
    }

    /**
     * Recursively inserts a book node into the correct BST position.
     */
    private Book insertRecursive(Book node, int isbn, String title, String author) {
        if (node == null) {
            return new Book(isbn, title, author);
        }
        if (isbn < node.isbn) {
            node.left = insertRecursive(node.left, isbn, title, author);
        } else if (isbn > node.isbn) {
            node.right = insertRecursive(node.right, isbn, title, author);
        }
        return node;
    }

    // --- Search -----------------------------------------------------------
    /**
     * Public method to search for a book by ISBN.
     * Delegates to a recursive helper for O(log n) average-case lookup.
     */
    public Book search(int isbn) {
        return searchRecursive(root, isbn);
    }

    /**
     * Recursively traverses the BST to locate a book with the given ISBN.
     * Time complexity: O(log n) average-case.
     */
    private Book searchRecursive(Book node, int isbn) {
        if (node == null) {
            return null; // not found
        }
        if (isbn == node.isbn) {
            return node;
        } else if (isbn < node.isbn) {
            return searchRecursive(node.left, isbn);
        } else {
            return searchRecursive(node.right, isbn);
        }
    }

    // --- Delete -----------------------------------------------------------
    /**
     * Public method to delete a book by ISBN from the BST.
     * Used when a book is borrowed (moved to the history stack).
     * @return true if deleted, false if not found.
     */
    public boolean delete(int isbn) {
        if (search(isbn) == null) {
            return false;
        }
        root = deleteRecursive(root, isbn);
        return true;
    }

    /**
     * Recursively removes the node with the given ISBN from the BST.
     */
    private Book deleteRecursive(Book node, int isbn) {
        if (node == null) {
            return null;
        }
        if (isbn < node.isbn) {
            node.left = deleteRecursive(node.left, isbn);
        } else if (isbn > node.isbn) {
            node.right = deleteRecursive(node.right, isbn);
        } else {
            // Node found — three cases:
            // Case 1 & 2: node has zero or one child
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            // Case 3: node has two children — replace with in-order successor
            Book successor = findMin(node.right);
            node.isbn = successor.isbn;
            node.title = successor.title;
            node.author = successor.author;
            node.right = deleteRecursive(node.right, successor.isbn);
        }
        return node;
    }

    /**
     * Finds the leftmost (minimum) node in a subtree.
     */
    private Book findMin(Book node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // --- Display (In-Order Traversal) -------------------------------------
    /**
     * Displays all books in the catalogue sorted by ISBN (in-order traversal).
     */
    public void displayAll() {
        if (root == null) {
            System.out.println("  (Catalogue is empty)");
            return;
        }
        inOrder(root);
    }

    private void inOrder(Book node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println("  " + node);
            inOrder(node.right);
        }
    }

    // --- Search by Title --------------------------------------------------
    /**
     * Public method to search for a book by title (O(n) traversal).
     */
    public Book searchByTitle(String title) {
        return searchByTitleRecursive(root, title);
    }

    private Book searchByTitleRecursive(Book node, String title) {
        if (node == null) {
            return null;
        }
        if (node.title.equalsIgnoreCase(title)) {
            return node;
        }
        Book leftResult = searchByTitleRecursive(node.left, title);
        if (leftResult != null) {
            return leftResult;
        }
        return searchByTitleRecursive(node.right, title);
    }

    // --- Count Total Books ------------------------------------------------
    /**
     * Public method to count the total number of books in the catalogue.
     */
    public int count() {
        return countRecursive(root);
    }

    private int countRecursive(Book node) {
        if (node == null) {
            return 0;
        }
        return 1 + countRecursive(node.left) + countRecursive(node.right);
    }
}
