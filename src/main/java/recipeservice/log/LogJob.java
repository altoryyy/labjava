package recipeservice.log;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogJob {
    private static final ConcurrentHashMap<Integer, String> jobStatus = new ConcurrentHashMap<>();

    static {
        initializeJobStatuses(); // Инициализируем статусы при загрузке класса
    }

    public static CompletableFuture<Integer> createLogFileAsync(String date) {
        int jobId = getNextAvailableId(); // Получаем следующий доступный ID
        jobStatus.put(jobId, "In Progress");

        return CompletableFuture.supplyAsync(() -> {
            try {
                String sourceFilePath = "logs/app.log";
                String targetLogFilePath = "logs/app.log." + jobId + "." + date + ".log";

                createLogFileFromSource(sourceFilePath, targetLogFilePath, date);
                jobStatus.put(jobId, "Completed");
                return jobId;
            } catch (Exception e) {
                jobStatus.put(jobId, "Failed: " + e.getMessage());
                return null;
            }
        });
    }

    private static void createLogFileFromSource(String sourceFilePath,
                                                String targetFilePath,
                                                String date) throws Exception {
        try (Stream<String> lines = Files.lines(Paths.get(sourceFilePath))) {
            List<String> filteredLines = lines.filter(line -> line.contains(date))
                    .collect(Collectors.toList());

            Files.write(Paths.get(targetFilePath), filteredLines, StandardOpenOption.CREATE);
        }
    }

    private static int getNextAvailableId() {
        try {
            return Files.list(Paths.get("logs"))
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        Pattern pattern = Pattern.compile("app\\.log\\.(\\d+)\\.");
                        Matcher matcher = pattern.matcher(fileName);
                        if (matcher.find()) {
                            return Integer.parseInt(matcher.group(1)); // Возвращаем ID, если совпадает
                        }
                        return -1; // Если не совпадает, возвращаем -1
                    })
                    .filter(id -> id != -1) // Пропускаем недействительные ID
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 1; // Увеличиваем на 1
        } catch (Exception e) {
            return 1; // Если возникла ошибка, начинаем с 1
        }
    }

    public static String getJobStatus(int jobId) {
        // Если ID не найден, проверяем существующие файлы
        if (!jobStatus.containsKey(jobId)) {
            return checkFileStatus(jobId);
        }
        return jobStatus.get(jobId);
    }

    private static String checkFileStatus(int jobId) {
        String filePath = "logs/app.log." + jobId + ".";
        try {
            // Проверяем, существует ли файл
            return Files.list(Paths.get("logs"))
                    .filter(path -> path.getFileName().toString().startsWith(filePath))
                    .findFirst()
                    .map(path -> "File exists: " + path.getFileName().toString())
                    .orElse("Not Found");
        } catch (Exception e) {
            return "Not Found";
        }
    }

    private static void initializeJobStatuses() {
        try {
            Files.list(Paths.get("logs"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        Pattern pattern = Pattern.compile("app\\.log\\.(\\d+)\\.");
                        Matcher matcher = pattern.matcher(fileName);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group(1));
                            jobStatus.put(id, "File exists: " + fileName);
                        }
                    });
        } catch (Exception e) {
        }
    }
}