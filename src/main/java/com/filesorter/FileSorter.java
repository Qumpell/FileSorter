package com.filesorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.filesorter.DirectoryPath.*;


public class FileSorter {

    private static final Path COUNT_FILE = Paths.get(HOME.getPath() + "/count.txt");
    private static final Map<String, Integer> moveCounts = new HashMap<>();

    static void processTheFile(Path filePath){
        String fileExtension = getFileExtension(filePath);

        String destinationPath = null;

        switch (fileExtension){
            case "jar":
                try{
                    ZonedDateTime fileCreationTime = getFileCreationTime(filePath);
                    if(fileCreationTime.getHour() % 2 == 0){
                        destinationPath = DEV.getPath();
                    }
                    else{
                        destinationPath = TEST.getPath();
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
                break;

            case "xml":
                destinationPath = DEV.getPath();
                break;

            default:
                break;

        }
        if(destinationPath != null){
            moveCounts.put(destinationPath, moveCounts.get(destinationPath) + 1);
            moveCounts.put("TOTAL", moveCounts.get("TOTAL") + 1);

            moveFileToGivenDirectory(filePath, Path.of(destinationPath));
            updateCountFile();
        }


    }

    private static String getFileExtension(Path pathToFile){
        String fileName = pathToFile.getFileName().toString();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private static ZonedDateTime getFileCreationTime(Path pathToFile) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(pathToFile, BasicFileAttributes.class);
        return ZonedDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
    }

    private static void updateCountFile() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : FileSorter.moveCounts.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            Files.write(COUNT_FILE, sb.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private static void moveFileToGivenDirectory(Path pathToFile, Path directoryToMove){
        try{
            Path destinationPath = directoryToMove.resolve(pathToFile.getFileName());
            Files.move(pathToFile, destinationPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   static void initializeMoveCounts(){
        moveCounts.put("TOTAL", 0);
        moveCounts.put(DEV.getPath(), 0);
        moveCounts.put(TEST.getPath(), 0);
        try{

            if(Files.notExists(COUNT_FILE)){
                Files.createFile(COUNT_FILE);
            }
           updateCountFile();

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
