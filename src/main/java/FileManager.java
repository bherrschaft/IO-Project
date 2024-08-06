import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(FileManager.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not set up logger configuration: " + e.toString());
        }
    }

    public static void displayDirectoryContents(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("The path specified is not a directory.");
            logger.warning("Attempted to display contents of a non-directory: " + directoryPath);
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            System.out.println("Contents of directory: " + directoryPath);
            for (File file : files) {
                String name = file.getName();
                long size = file.length();
                String lastModified = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified());
                System.out.printf("%-20s %-10d %-20s%n", name, size, lastModified);
            }
            logger.info("Displayed contents of directory: " + directoryPath);
        } else {
            System.out.println("Unable to access directory contents.");
            logger.warning("Failed to access directory contents: " + directoryPath);
        }
        System.out.flush();
    }

    public static void copyFile(String sourcePath, String destinationPath) {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        try {
            if (!Files.exists(source)) {
                System.err.println("Source file does not exist: " + sourcePath);
                logger.warning("Attempted to copy from a non-existent source: " + sourcePath);
                return;
            }

            if (Files.isDirectory(destination)) {
                destination = destination.resolve(source.getFileName());
            }

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
            logger.info("Copied file from " + source + " to " + destination);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Destination file already exists: " + destination);
            logger.warning("Failed to copy because the destination file already exists: " + destination);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Destination directory is not empty: " + destination);
            logger.warning("Failed to copy because the destination directory is not empty: " + destination);
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
            logger.severe("Failed to copy file from " + source + " to " + destination + " - " + e.getMessage());
        }
        System.out.flush();
    }

    public static void moveFile(String sourcePath, String destinationPath) {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        try {
            if (!Files.exists(source)) {
                System.err.println("Source file does not exist: " + sourcePath);
                logger.warning("Attempted to move a non-existent source: " + sourcePath);
                return;
            }

            if (Files.isDirectory(destination)) {
                destination = destination.resolve(source.getFileName());
            }

            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully.");
            logger.info("Moved file from " + source + " to " + destination);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Destination file already exists: " + destination);
            logger.warning("Failed to move because the destination file already exists: " + destination);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Destination directory is not empty: " + destination);
            logger.warning("Failed to move because the destination directory is not empty: " + destination);
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
            logger.severe("Failed to move file from " + source + " to " + destination + " - " + e.getMessage());
        }
        System.out.flush();
    }

    public static void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
            System.out.println("File deleted successfully.");
            logger.info("Deleted file " + filePath);
        } catch (NoSuchFileException e) {
            System.err.println("File does not exist: " + filePath);
            logger.warning("Attempted to delete a non-existent file: " + filePath);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Directory is not empty: " + filePath);
            logger.warning("Attempted to delete a non-empty directory as a file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            logger.severe("Failed to delete file " + filePath + " - " + e.getMessage());
        }
        System.out.flush();
    }

    public static void createDirectory(String directoryPath) {
        try {
            Files.createDirectory(Paths.get(directoryPath));
            System.out.println("Directory created successfully.");
            logger.info("Created directory " + directoryPath);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Directory already exists: " + directoryPath);
            logger.warning("Attempted to create a directory that already exists: " + directoryPath);
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
            logger.severe("Failed to create directory " + directoryPath + " - " + e.getMessage());
        }
        System.out.flush();
    }

    public static void deleteDirectory(String directoryPath) {
        try {
            Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("Directory deleted successfully.");
            logger.info("Deleted directory " + directoryPath);
        } catch (NoSuchFileException e) {
            System.err.println("Directory does not exist: " + directoryPath);
            logger.warning("Attempted to delete a non-existent directory: " + directoryPath);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Directory is not empty: " + directoryPath);
            logger.warning("Failed to delete because the directory is not empty: " + directoryPath);
        } catch (IOException e) {
            System.err.println("Error deleting directory: " + e.getMessage());
            logger.severe("Failed to delete directory " + directoryPath + " - " + e.getMessage());
        }
        System.out.flush();
    }

    public static List<String> searchFiles(String directoryPath, String searchQuery) {
        List<String> matchedFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    matchedFiles.addAll(searchFiles(entry.toString(), searchQuery)); // Recursive search
                } else if (entry.getFileName().toString().contains(searchQuery)) {
                    matchedFiles.add(entry.toString());
                }
            }
            logger.info("Searched files in directory: " + directoryPath + " with query: " + searchQuery);
        } catch (IOException e) {
            System.err.println("Error searching files: " + e.getMessage());
            logger.severe("Failed to search files in directory " + directoryPath + " - " + e.getMessage());
        }
        return matchedFiles;
    }
}
