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
                    "fileKey", attrs.fileKey().toString(), //Waar het bestand exact op het schijf is (vb: (dev=1000010,ino=74397868))
                    "size", attrs.size(), //Geeft je het bestand terug in Bytes
                    "created", attrs.creationTime().toString(),
                    "modified", attrs.lastModifiedTime().toString()
            );

        } catch (IOException e) {
            System.out.println("Error \n" + e); /// Issue fix: https://stackoverflow.com/questions/10477607/avoid-printstacktrace-use-a-logger-call-instead
            return Map.of(); //Mocht er iets gebeuren, stuurt die een dictionary.
        }
    }

    public void createFile(String fileName) {
        Path p = Paths.get(fileName);

        try {
            Files.createFile(p);
        } catch (IOException e) {
            System.out.println("Error \n" + e);
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
        String file = filePath + "/" + fileName;
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
