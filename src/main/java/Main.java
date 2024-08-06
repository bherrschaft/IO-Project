import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
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
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter directory path: ");
                    String dirPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.displayDirectoryContents(dirPath);
                    break;
                case 2:
                    System.out.print("Enter source file path: ");
                    String sourceCopyPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    System.out.print("Enter destination file path: ");
                    String destinationCopyPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.copyFile(sourceCopyPath, destinationCopyPath);
                    break;
                case 3:
                    System.out.print("Enter source file path: ");
                    String sourceMovePath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    System.out.print("Enter destination file path: ");
                    String destinationMovePath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.moveFile(sourceMovePath, destinationMovePath);
                    break;
                case 4:
                    System.out.print("Enter file path to delete: ");
                    String deleteFilePath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.deleteFile(deleteFilePath);
                    break;
                case 5:
                    System.out.print("Enter directory path to create: ");
                    String createDirPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.createDirectory(createDirPath);
                    break;
                case 6:
                    System.out.print("Enter directory path to delete: ");
                    String deleteDirPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    FileManager.deleteDirectory(deleteDirPath);
                    break;
                case 7:
                    System.out.print("Enter directory path to search: ");
                    String searchDirPath = scanner.nextLine().trim(); // Trim any leading or trailing spaces
                    System.out.print("Enter search query: ");
                    String searchQuery = scanner.nextLine();
                    List<String> results = FileManager.searchFiles(searchDirPath, searchQuery);
                    System.out.println("Search Results:");
                    for (String result : results) {
                        System.out.println(result);
                    }
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
