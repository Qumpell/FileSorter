package com.filesorter;

import java.util.List;

import static com.filesorter.DirectoryPath.*;

public class Main {
    public static void main(String[] args) {

        run();

    }

    private static void run() {
        List<DirectoryPath> directoryPaths = List.of(HOME, DEV, TEST);
        DirectoryManager.createDirectories(directoryPaths);

        DirectoryManager.watchDirectory(HOME.getPath());
    }
    }


