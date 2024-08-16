package com.filesorter;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryManager {

    static void createDirectory(String directory) {
        try{
            Files.createDirectory(Path.of(directory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void createDirectories(List<DirectoryPath> pathList){
        for(DirectoryPath path : pathList){
            if(!directoryExists(path.getPath())){
                createDirectory(path.getPath());
            }
        }
    }

    private static boolean directoryExists(String directory){
        return Files.exists(Path.of(directory));
    }

    static void watchDirectory(String directory){
        FileSorter.initializeMoveCounts();

        Path directoryToWatch = Path.of(directory);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            directoryToWatch.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            System.out.println("Monitoring directory: " + directoryToWatch.toAbsolutePath());

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take(); // Wait for key to be signaled
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();

                    if (kind == ENTRY_CREATE) {
                        System.out.println("File created: " + fileName);

                        FileSorter.processTheFile(directoryToWatch.resolve(fileName));

                    } else if (kind == ENTRY_DELETE) {
                        System.out.println("File deleted: " + fileName);
                    } else if (kind == ENTRY_MODIFY) {
                        System.out.println("File modified: " + fileName);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
