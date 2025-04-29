package recipeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @GetMapping("/download")
    @Operation(summary = "Скачать файл логов",
            description = "Создает и загружает файл логов за указанный день.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Файл логов успешно загружен"),
        @ApiResponse(responseCode = "404", description = "Файл логов не найден")
    })
    public ResponseEntity<byte[]> downloadLogFile(@RequestParam String date) {
        String sourceLogFilePath = "logs/app.log";
        String targetLogFilePath = "logs/app.log." + date + ".log";

        try {
            createLogFileFromSource(sourceLogFilePath, targetLogFilePath, date);

            byte[] logFileContent = Files.readAllBytes(Paths.get(targetLogFilePath));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=logfile_" + date + ".log")
                    .body(logFileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void createLogFileFromSource(String sourceFilePath,
                                         String targetFilePath,
                                         String date) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(sourceFilePath))) {
            List<String> filteredLines = lines.filter(line -> line.contains(date))
                    .collect(Collectors.toList());

            Files.write(Paths.get(targetFilePath), filteredLines, StandardOpenOption.CREATE);
        }
    }
}