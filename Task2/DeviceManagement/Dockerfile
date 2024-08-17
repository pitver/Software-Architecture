# 1. Используем образ Maven для сборки приложения
FROM maven:3.8.6-eclipse-temurin-17 AS build

# 2. Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# 3. Копируем файлы проекта Maven в контейнер
COPY pom.xml .

# 4. Загружаем зависимости проекта
RUN mvn dependency:go-offline -B

# 5. Копируем оставшиеся файлы проекта
COPY src ./src

# 6. Собираем приложение
RUN mvn clean install -DskipTests

# 7. Используем легковесный образ JDK для запуска приложения
FROM eclipse-temurin:17-jdk-alpine

# 8. Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# 9. Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /app/target/DeviceManagement-0.0.1-SNAPSHOT.jar /app/device-management.jar

# 10. Открываем порт, на котором будет работать приложение
EXPOSE 8081

# 11. Устанавливаем точку входа для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "/app/device-management.jar"]
