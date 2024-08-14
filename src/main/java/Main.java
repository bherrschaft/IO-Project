import java.util.List; // Import the List interface from the java.util package
import java.util.Scanner; // Import the Scanner class from the java.util package

public class Main {

    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner scanner = new Scanner(System.in);
        boolean running = true; // Boolean variable to control the main loop

        // Main loop to keep the program running until the user chooses to exit
        while (running) {
            // Display the menu options to the user
            System.out.println("\nFile Manager");
            System.out.println("1. Display Directory Contents");
            System.out.println("2. Copy File");
            System.out.println("3. Move File");
            System.out.println("4. Delete File");
            System.out.println("5. Create Directory");
            System.out.println("6. Delete Directory");
            System.out.println("7. Search Files");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            // Read the user's choice as an integer
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            // Switch statement to handle the user's choice
            switch (choice) {
                case 1:
                    // Option 1: Display directory contents
                    System.out.print("Enter directory path: ");
                    String dirPath = scanner.nextLine().trim(); // Read and trim the directory path
                    FileManager.displayDirectoryContents(dirPath); // Call the method to display directory contents
                    break;
                case 2:
                    // Option 2: Copy a file
                    System.out.print("Enter source file path: ");
                    String sourceCopyPath = scanner.nextLine().trim(); // Read and trim the source file path
                    System.out.print("Enter destination file path: ");
                    String destinationCopyPath = scanner.nextLine().trim(); // Read and trim the destination file path
                    FileManager.copyFile(sourceCopyPath, destinationCopyPath); // Call the method to copy the file
                    break;
                case 3:
                    // Option 3: Move a file
                    System.out.print("Enter source file path: ");
                    String sourceMovePath = scanner.nextLine().trim(); // Read and trim the source file path
                    System.out.print("Enter destination file path: ");
                    String destinationMovePath = scanner.nextLine().trim(); // Read and trim the destination file path
                    FileManager.moveFile(sourceMovePath, destinationMovePath); // Call the method to move the file
                    break;
                case 4:
                    // Option 4: Delete a file
                    System.out.print("Enter file path to delete: ");
                    String deleteFilePath = scanner.nextLine().trim(); // Read and trim the file path
                    FileManager.deleteFile(deleteFilePath); // Call the method to delete the file
                    break;
                case 5:
                    // Option 5: Create a directory
                    System.out.print("Enter directory path to create: ");
                    String createDirPath = scanner.nextLine().trim(); // Read and trim the directory path
                    FileManager.createDirectory(createDirPath); // Call the method to create the directory
                    break;
                case 6:
                    // Option 6: Delete a directory
                    System.out.print("Enter directory path to delete: ");
                    String deleteDirPath = scanner.nextLine().trim(); // Read and trim the directory path
                    FileManager.deleteDirectory(deleteDirPath); // Call the method to delete the directory
                    break;
                case 7:
                    // Option 7: Search for files in a directory
                    System.out.print("Enter directory path to search: ");
                    String searchDirPath = scanner.nextLine().trim(); // Read and trim the directory path
                    System.out.print("Enter search query: ");
                    String searchQuery = scanner.nextLine(); // Read the search query
                    List<String> results = FileManager.searchFiles(searchDirPath, searchQuery); // Call the method to search for files
                    System.out.println("Search Results:");
                    // Iterate over the list of results and print each one
                    for (String result : results) {
                        System.out.println(result);
                    }
                    break;
                case 8:
                    // Option 8: Exit the program
                    running = false; // Set running to false to exit the loop
                    break;
                default:
                    // Handle invalid options
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close(); // Close the scanner to release resources
    }
}

/*
 * Summary:
 * This Main class provides a command-line interface for a file management system.
 * It presents a menu with options to display directory contents, copy, move, and delete files,
 * create and delete directories, and search for files. The user interacts with the program by selecting
 * options and providing necessary inputs like file paths. The program continues to run in a loop
 * until the user chooses to exit.
 */
