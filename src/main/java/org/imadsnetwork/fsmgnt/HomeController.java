package org.imadsnetwork.fsmgnt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@Controller //Vertelt java om dit klasse gebruikt moet worden als Controller
public class HomeController {

    @GetMapping("/drives")
    @ResponseBody
    public File[] getDrives() {
        /*
            Haalt alle schijf letters (Windows) en zet deze in een array
            macOS haalt alle "volumes" (in "/Volumes") op en zet die in hetzelfde array
        */

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return File.listRoots();
        }

        if (os.contains("mac")) {
            File volumes = new File("/Volumes");
            File[] allDrives = volumes.listFiles();

            if (allDrives == null) {
                return new File[0];
            }

            return Arrays.stream(allDrives)
                    .filter(f -> !f.getName().contains("hidden"))
                    .toArray(File[]::new);
        }


        // Mocht het OS toch niet ondersteund worden
        return new File[0];
    }

    @GetMapping("/fileMeta/path/{*pathString}")
    @ResponseBody
    public Map<String, Object> fileMetaTest(@PathVariable String pathString, @RequestParam String fileName) {
        FileMG file = new FileMG(pathString);
        return file.getFileMetadata(fileName);
    }

    @GetMapping("/getFiles/path/{*rest}")
    @ResponseBody
    public String handle(@PathVariable String rest) {
        return rest;

    }

    @GetMapping("/getContents/path/{*filePath}")
    @ResponseBody
    public ResponseEntity<byte[]> getFileContents(@PathVariable String filePath, @RequestParam String fileName) {
        String[] extension = fileName.split("\\.");
        FileMG file = new FileMG(filePath);

        byte[] fileData = file.getData(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType(extension[1]))
                .body(fileData);
    }

    public String contentType(String extension){
        String lowercased = extension.toLowerCase();

        return switch (lowercased) {
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "json" -> "application /json";
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case "ogg" -> "video/ogg";
            case "avi" -> "video/x-msvideo";
            case "mov" -> "video/quicktime";

            default -> "application/octet-stream";
        };
    }
}

