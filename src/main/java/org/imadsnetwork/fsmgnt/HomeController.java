package org.imadsnetwork.fsmgnt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

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

    @GetMapping("/fileMeta")
    @ResponseBody
    public Map<String, Object> fileMetaTest(@RequestParam String fileName) {
        FileMG file = new FileMG("/Users/imad/Downloads");
        return file.getFileMetadata(fileName);
    }

    @GetMapping("/getContents/path/{*rest}")
    @ResponseBody
    public String handle(@PathVariable String rest) {
        return rest;
    }
}

