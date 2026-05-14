import java.util.Scanner;

/**
 * Entry point for the Smart Library system.
 * Provides a menu-driven console interface with full input validation
 * using try-catch blocks and Scanner buffer clearing.
 */
public class SmartLibrary {

    private static final String LINE  = "=============================================";
    private static final String TITLE = "     SMART LIBRARY MANAGEMENT SYSTEM";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SmartLibraryImpl library = new SmartLibraryImpl();

        // Welcome banner
        System.out.println();
        System.out.println(LINE);
        System.out.println(TITLE);
        System.out.println(LINE);

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("  Enter your choice (1-6): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n  [ERROR] Invalid input! Please enter a number between 1 and 6.");
                continue;
            }

            switch (choice) {
                case 1:
                    addBookMenu(scanner, library);
                    break;
                case 2:
                    searchBookMenu(scanner, library);
                    break;
                case 3:
                    borrowBookMenu(scanner, library);
                    break;
                case 4:
                    library.viewHistory();
                    break;
                case 5:
                    library.displayCatalogue();
                    break;
                case 6:
                    running = false;
                    System.out.println("\n  Thank you for using Smart Library. Goodbye!");
                    break;
                default:
                    System.out.println("\n  [ERROR] Invalid choice! Please select a number between 1 and 6.");
            }
        }

        scanner.close();
    }

    /**
     * Prints the main menu options.
     */
    private static void printMenu() {
        System.out.println("\n" + LINE);
        System.out.println("  MAIN MENU");
        System.out.println(LINE);
        System.out.println("  1. Add Book");
        System.out.println("  2. Search Book");
        System.out.println("  3. Borrow Book");
        System.out.println("  4. View Borrowing History");
        System.out.println("  5. Display Catalogue");
        System.out.println("  6. Exit");
        System.out.println(LINE);
    }

    // ---- Sub-menus with input validation --------------------------------

    /**
     * Handles the "Add Book" workflow with ISBN validation via try-catch.
     */
    private static void addBookMenu(Scanner scanner, SmartLibraryImpl library) {
        System.out.println("\n  --- Add a New Book ---");

        int isbn;
        while (true) {
            System.out.print("  Enter ISBN (positive integer): ");
            try {
                isbn = Integer.parseInt(scanner.nextLine().trim());
                if (isbn <= 0) {
                    System.out.println("  [ERROR] ISBN must be a positive integer. Try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid ISBN! Please enter a valid integer.");
            }
        }

        System.out.print("  Enter Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("  [ERROR] Title cannot be empty. Operation cancelled.");
            return;
        }

        System.out.print("  Enter Author: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("  [ERROR] Author cannot be empty. Operation cancelled.");
            return;
        }

        library.addBook(isbn, title, author);
    }

    /**
     * Handles the "Search Book" workflow with ISBN validation via try-catch.
     */
    private static void searchBookMenu(Scanner scanner, SmartLibraryImpl library) {
        System.out.println("\n  --- Search for a Book ---");

        int isbn;
        while (true) {
            System.out.print("  Enter ISBN to search: ");
            try {
                isbn = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid ISBN! Please enter a valid integer.");
            }
        }

        library.searchBook(isbn);
    }

    /**
     * Handles the "Borrow Book" workflow with ISBN validation via try-catch.
     */
    private static void borrowBookMenu(Scanner scanner, SmartLibraryImpl library) {
        System.out.println("\n  --- Borrow a Book ---");

        int isbn;
        while (true) {
            System.out.print("  Enter ISBN to borrow: ");
            try {
                isbn = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid ISBN! Please enter a valid integer.");
            }
        }

        library.borrowBook(isbn);
    }
}
