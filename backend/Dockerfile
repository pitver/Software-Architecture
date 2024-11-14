# Используем базовый образ с JDK 17, совместимый со Spring Boot
FROM eclipse-temurin:17-jdk-alpine as builder

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем Gradle/Maven файлы и исходники
COPY . .

# Сборка приложения с Gradle/Maven
# Если вы используете Maven:
RUN ./mvnw clean package -DskipTests

# Если вы используете Gradle:
# RUN ./gradlew clean build -x test

# Создаем отдельный минимальный образ для запуска приложения
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию для запуска
WORKDIR /app

# Копируем скомпилированный jar файл из предыдущего шага сборки
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт для приложения (по умолчанию 8080)
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
