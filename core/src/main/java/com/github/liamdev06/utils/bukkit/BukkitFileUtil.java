package com.github.liamdev06.utils.bukkit;

import com.github.liamdev06.LPlugin;
import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;

@UtilityClass
public class BukkitFileUtil {

    /**
     * Sets up a plugin file in the plugin data folder.
     *
     * @param plugin Instance of {@link LPlugin}.
     * @param fileName The name of the file to set up.
     * @return Instance of the set up file.
     */
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
            throw new RuntimeException("", exception);
        }
        return file;
    }
}