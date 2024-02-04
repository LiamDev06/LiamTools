package com.github.liamdev06.utils.java;

import com.github.liamdev06.LPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;

/**
 * Utility for working with and modifying files.
 */
public class FileUtil {

    public static @NonNull File setupPluginFile(@NonNull LPlugin plugin, @NonNull String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (file.exists()) {
            return file;
        }

        try (InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (file.createNewFile() && inputStream != null) {
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file;
    }

}