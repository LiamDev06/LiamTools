package com.github.liamdev06.utils.java;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Paths;

/**
 * Utility class for working with {@link File} and performing file operations.
 */
@UtilityClass
public class JavaFileUtil {

    /**
     *
     * @param file
     * @return
     */
    public static @NonNull String findFileExtension(@NonNull File file) {
        return findFileExtension(file.getName());
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static @NonNull String findFileExtension(@NonNull String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return fileName; // No extension found
        }
        return fileName.substring(lastIndex + 1);
    }

    /**
     * Removes a file extension and returns the name without the extension.
     * <p>
     * For example {@code data.json} becomes {@code data}.
     *
     * @param file Instance of the file whose name will be stripped its extension.
     * @return The file name without its extension.
     * @see #removeFileExtension(String)
     */
    public static @NonNull String removeFileExtension(@NonNull File file) {
        return removeFileExtension(file.getName());
    }

    /**
     * Removes a file extension from a file name.
     *
     * @param fileName The name of the file.
     * @return The file name without its extension.
     */
    public static @NonNull String removeFileExtension(@NonNull String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return fileName; // No extension found
        }
        return fileName.substring(0, lastIndex);
    }

    /**
     * Deletes a target file using some extra logic than {@link File#delete()}.
     * Starts by checking if the file is valid, if so tries to delete the file at the spot.
     * If the file could not be deleted, it is "queued" to be deleted on application shutdown.
     *
     * @param fileToDelete Instance of the {@link File} to delete.
     */
    public static void deleteFile(@Nullable File fileToDelete) {
        // File is not valid to be deleted
        if (fileToDelete == null || !fileToDelete.exists()) {
            return;
        }

        if (!fileToDelete.delete()) {
            fileToDelete.deleteOnExit();
        }
    }

    /**
     * Downloads a file from a {@link URI} to a specified location.
     *
     * @param uri The URI to get the file from.
     * @param targetDirectory The target directory where the file should end up in.
     * @param fileName Set the name the downloaded output file should have.
     *                 Set to null to ignore and use the name in the URI.
     */
    public static void downloadFileFromUri(@NonNull URI uri, @NonNull String targetDirectory, @Nullable String fileName) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (fileName == null) {
                    fileName = Paths.get(uri.getPath()).getFileName().toString();
                }

                File outputFile = new File(targetDirectory, fileName);
                try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                     FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            connection.disconnect();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}