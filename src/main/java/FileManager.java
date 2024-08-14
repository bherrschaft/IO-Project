import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class FileManager {

    // Logger instance to record log messages for the FileManager class
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    // Static block to configure the logger settings from a properties file
    static {
        try {
            // Attempt to read logger configuration from a file named 'logging.properties' located in the classpath
            LogManager.getLogManager().readConfiguration(FileManager.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            // If an error occurs during configuration, print an error message to the standard error stream
            System.err.println("Could not set up logger configuration: " + e.toString());
        }
    }

    // Method to display the contents of a directory specified by directoryPath
    public static void displayDirectoryContents(String directoryPath) {
        // Create a File object representing the directory at the given path
        File directory = new File(directoryPath);

        // Check if the provided path is not a directory
        if (!directory.isDirectory()) {
            // Print a message indicating the path is not a directory
            System.out.println("The path specified is not a directory.");
            // Log a warning that an invalid directory path was provided
            logger.warning("Attempted to display contents of a non-directory: " + directoryPath);
            return; // Exit the method as there is nothing more to do
        }

        // Retrieve an array of File objects representing all files and directories within the directory
        File[] files = directory.listFiles();

        // Check if the directory contents could be retrieved
        if (files != null) {
            // Print the directory path and start listing the contents
            System.out.println("Contents of directory: " + directoryPath);
            for (File file : files) { // Iterate over each file or directory in the array
                String name = file.getName(); // Get the name of the file or directory
                long size = file.length(); // Get the size of the file in bytes
                // Format the last modified time of the file or directory using a specific date-time pattern
                String lastModified = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified());
                // Print the file name, size, and last modified time in a formatted manner
                System.out.printf("%-20s %-10d %-20s%n", name, size, lastModified);
            }
            // Log an informational message that the directory contents were displayed
            logger.info("Displayed contents of directory: " + directoryPath);
        } else {
            // Print a message indicating that the directory contents could not be accessed
            System.out.println("Unable to access directory contents.");
            // Log a warning message that the directory contents could not be accessed
            logger.warning("Failed to access directory contents: " + directoryPath);
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to copy a file from sourcePath to destinationPath
    public static void copyFile(String sourcePath, String destinationPath) {
        // Create Path objects representing the source file and destination path
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        try {
            // Check if the source file exists
            if (!Files.exists(source)) {
                // Print an error message if the source file does not exist
                System.err.println("Source file does not exist: " + sourcePath);
                // Log a warning that an attempt was made to copy from a non-existent source
                logger.warning("Attempted to copy from a non-existent source: " + sourcePath);
                return; // Exit the method since the copy operation cannot proceed
            }

            // Check if the destination path is a directory
            if (Files.isDirectory(destination)) {
                // Resolve the destination path by appending the source file name to the destination directory
                destination = destination.resolve(source.getFileName());
            }

            // Perform the file copy, replacing any existing file at the destination
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            // Print a message indicating the file was copied successfully
            System.out.println("File copied successfully.");
            // Log an informational message that the file was copied from the source to the destination
            logger.info("Copied file from " + source + " to " + destination);
        } catch (FileAlreadyExistsException e) {
            // Handle the case where the destination file already exists
            System.err.println("Destination file already exists: " + destination);
            // Log a warning that the copy operation failed because the destination file already exists
            logger.warning("Failed to copy because the destination file already exists: " + destination);
        } catch (DirectoryNotEmptyException e) {
            // Handle the case where the destination directory is not empty
            System.err.println("Destination directory is not empty: " + destination);
            // Log a warning that the copy operation failed because the destination directory is not empty
            logger.warning("Failed to copy because the destination directory is not empty: " + destination);
        } catch (IOException e) {
            // Handle other I/O exceptions that may occur during the copy operation
            System.err.println("Error copying file: " + e.getMessage());
            // Log a severe error indicating that the file copy operation failed
            logger.severe("Failed to copy file from " + source + " to " + destination + " - " + e.getMessage());
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to move a file from sourcePath to destinationPath
    public static void moveFile(String sourcePath, String destinationPath) {
        // Create Path objects representing the source file and destination path
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        try {
            // Check if the source file exists
            if (!Files.exists(source)) {
                // Print an error message if the source file does not exist
                System.err.println("Source file does not exist: " + sourcePath);
                // Log a warning that an attempt was made to move a non-existent source
                logger.warning("Attempted to move a non-existent source: " + sourcePath);
                return; // Exit the method since the move operation cannot proceed
            }

            // Check if the destination path is a directory
            if (Files.isDirectory(destination)) {
                // Resolve the destination path by appending the source file name to the destination directory
                destination = destination.resolve(source.getFileName());
            }

            // Perform the file move, replacing any existing file at the destination
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            // Print a message indicating the file was moved successfully
            System.out.println("File moved successfully.");
            // Log an informational message that the file was moved from the source to the destination
            logger.info("Moved file from " + source + " to " + destination);
        } catch (FileAlreadyExistsException e) {
            // Handle the case where the destination file already exists
            System.err.println("Destination file already exists: " + destination);
            // Log a warning that the move operation failed because the destination file already exists
            logger.warning("Failed to move because the destination file already exists: " + destination);
        } catch (DirectoryNotEmptyException e) {
            // Handle the case where the destination directory is not empty
            System.err.println("Destination directory is not empty: " + destination);
            // Log a warning that the move operation failed because the destination directory is not empty
            logger.warning("Failed to move because the destination directory is not empty: " + destination);
        } catch (IOException e) {
            // Handle other I/O exceptions that may occur during the move operation
            System.err.println("Error moving file: " + e.getMessage());
            // Log a severe error indicating that the file move operation failed
            logger.severe("Failed to move file from " + source + " to " + destination + " - " + e.getMessage());
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to delete a file at the given filePath
    public static void deleteFile(String filePath) {
        try {
            // Delete the file at the specified filePath
            Files.delete(Paths.get(filePath));
            // Print a message indicating the file was deleted successfully
            System.out.println("File deleted successfully.");
            // Log an informational message that the file was deleted
            logger.info("Deleted file " + filePath);
        } catch (NoSuchFileException e) {
            // Handle the case where the file does not exist
            System.err.println("File does not exist: " + filePath);
            // Log a warning that an attempt was made to delete a non-existent file
            logger.warning("Attempted to delete a non-existent file: " + filePath);
        } catch (DirectoryNotEmptyException e) {
            // Handle the case where the path points to a non-empty directory instead of a file
            System.err.println("Directory is not empty: " + filePath);
            // Log a warning that the deletion failed because the directory is not empty
            logger.warning("Attempted to delete a non-empty directory as a file: " + filePath);
        } catch (IOException e) {
            // Handle other I/O exceptions that may occur during the delete operation
            System.err.println("Error deleting file: " + e.getMessage());
            // Log a severe error indicating that the file deletion failed
            logger.severe("Failed to delete file " + filePath + " - " + e.getMessage());
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to create a directory at the given directoryPath
    public static void createDirectory(String directoryPath) {
        try {
            // Create a new directory at the specified directoryPath
            Files.createDirectory(Paths.get(directoryPath));
            // Print a message indicating the directory was created successfully
            System.out.println("Directory created successfully.");
            // Log an informational message that the directory was created
            logger.info("Created directory " + directoryPath);
        } catch (FileAlreadyExistsException e) {
            // Handle the case where the directory already exists
            System.err.println("Directory already exists: " + directoryPath);
            // Log a warning that an attempt was made to create an existing directory
            logger.warning("Attempted to create a directory that already exists: " + directoryPath);
        } catch (IOException e) {
            // Handle other I/O exceptions that may occur during the directory creation
            System.err.println("Error creating directory: " + e.getMessage());
            // Log a severe error indicating that the directory creation failed
            logger.severe("Failed to create directory " + directoryPath + " - " + e.getMessage());
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to delete a directory and its contents at the given directoryPath
    public static void deleteDirectory(String directoryPath) {
        try {
            // Walk the file tree starting from the directoryPath and visit each file and directory
            Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Delete each file in the directory
                    Files.delete(file);
                    return FileVisitResult.CONTINUE; // Continue to the next file
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // Delete the directory itself after all its contents have been deleted
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE; // Continue after the directory is deleted
                }
            });
            // Print a message indicating the directory was deleted successfully
            System.out.println("Directory deleted successfully.");
            // Log an informational message that the directory was deleted
            logger.info("Deleted directory " + directoryPath);
        } catch (NoSuchFileException e) {
            // Handle the case where the directory does not exist
            System.err.println("Directory does not exist: " + directoryPath);
            // Log a warning that an attempt was made to delete a non-existent directory
            logger.warning("Attempted to delete a non-existent directory: " + directoryPath);
        } catch (DirectoryNotEmptyException e) {
            // Handle the case where the directory is not empty
            System.err.println("Directory is not empty: " + directoryPath);
            // Log a warning that the directory deletion failed because it is not empty
            logger.warning("Failed to delete because the directory is not empty: " + directoryPath);
        } catch (IOException e) {
            // Handle other I/O exceptions that may occur during the directory deletion
            System.err.println("Error deleting directory: " + e.getMessage());
            // Log a severe error indicating that the directory deletion failed
            logger.severe("Failed to delete directory " + directoryPath + " - " + e.getMessage());
        }
        System.out.flush(); // Ensure that any buffered output is sent to the console immediately
    }

    // Method to search for files in a directory that match the searchQuery
    public static List<String> searchFiles(String directoryPath, String searchQuery) {
        List<String> matchedFiles = new ArrayList<>(); // List to store matched file paths

        // Open a directory stream to iterate over the files and directories in the specified directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path entry : stream) { // Iterate over each entry in the directory
                if (Files.isDirectory(entry)) { // If the entry is a directory
                    // Recursively search for files in the subdirectory
                    matchedFiles.addAll(searchFiles(entry.toString(), searchQuery));
                } else if (entry.getFileName().toString().contains(searchQuery)) { // If the file name contains the search query
                    matchedFiles.add(entry.toString()); // Add the matching file path to the list
                }
            }
            // Log an informational message that the search was completed
            logger.info("Searched files in directory: " + directoryPath + " with query: " + searchQuery);
        } catch (IOException e) {
            // Handle I/O exceptions that may occur during the search
            System.err.println("Error searching files: " + e.getMessage());
            // Log a severe error indicating that the file search failed
            logger.severe("Failed to search files in directory " + directoryPath + " - " + e.getMessage());
        }
        return matchedFiles; // Return the list of matched files
    }
}

/*
 * Summary:
 * The FileManager class provides utility methods for managing files and directories, including:
 * - Displaying directory contents
 * - Copying, moving, and deleting files
 * - Creating and deleting directories
 * - Searching for files within a directory
 * The class uses a logger to record information, warnings, and errors during file operations.
 * Each method handles common file system errors, such as non-existent files or directories, and logs appropriate messages.
 */
