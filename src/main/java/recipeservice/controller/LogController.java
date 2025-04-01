package recipeservice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @GetMapping("/download")
    public byte[] downloadLogFile(@RequestParam String date) throws IOException {
        String logFilePath = "/Users/artemlopatin/RecipeService/logs/app.log";
        return Files.readAllBytes(Paths.get(logFilePath));
    }
}