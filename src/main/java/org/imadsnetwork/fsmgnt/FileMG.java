package org.imadsnetwork.fsmgnt;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileMG implements FileManagement {
    String filePath;
    public FileMG(String filePath){
        this.filePath = filePath;
    }

    public Map<String, Object> getFileMetadata(String fileName) {
        try {
            Path p = Paths.get(filePath + "/" + fileName); //Converteert de String 'filePath' naar een Path object dat we kunnen gebruiken
            BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);

            return Map.of(
                    "fileKey", attrs.fileKey().toString(), //Waar het bestand exact op het schijf zit op de inode (vb: (dev=1000010,ino=74397868))
                    "size", attrs.size(), //Geeft je het bestand terug in Bytes
                    "created", attrs.creationTime().toString(),
                    "modified", attrs.lastModifiedTime().toString()
            );

        } catch (IOException e) {
            System.out.println("Error \n" + e); /// Issue fix: https://stackoverflow.com/questions/10477607/avoid-printstacktrace-use-a-logger-call-instead
            return Map.of(); //Mocht er iets gebeuren, stuurt die een lege dictionary.
        }
    }

    public List<Map<String, Object>> listDirectoryFiles() {
        List<Map<String, Object>> result = new ArrayList<>();
        Path dir = Paths.get(filePath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                result.add(getFileMetadata(path.getFileName().toString()));
            }
        } catch (IOException e) {
            System.out.println("Error \n" + e);
        }

        return result;
    }


    public Boolean createFile(String fileName) {
        Path p = Paths.get(filePath, fileName);

        try {
            Files.createFile(p);
            return true;
        } catch (IOException e) {
            System.out.println("Error \n" + e);
            return false;
        }
    }

    public Boolean uploadFile(String fileName, byte[] data) {
        Path p = Paths.get(filePath, fileName);

        try {
            Files.write(p, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING); //Maakt het bestand volledig opnieuw.
            return true;
        } catch (IOException e) {
            System.out.println("Error\n" + e);
            return false;
        }
    }


    public byte[] getData(String fileName) {
        Path path = Paths.get(filePath, fileName);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    public FileStatus deleteFile(String fileName, boolean permanent){
        Path p = Paths.get(filePath, fileName);

        try{
            Files.delete(p);
            return FileStatus.SUCCESS;
        }catch(IOException e){
            System.out.println("Error \n" + e);
            return FileStatus.FAILURE;
        }
    }
}
