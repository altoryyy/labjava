# Используем базовый образ с JDK и Maven
FROM maven:3.8.5-openjdk-17-slim AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и зависимости
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Используем образ JDK для выполнения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл из предыдущего этапа
COPY --from=build /app/target/RecipeService-0.0.1-SNAPSHOT.jar recipe-service.jar

# Запускаем приложение
CMD ["java", "-jar", "recipe-service.jar"]