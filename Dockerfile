FROM openjdk:17-jdk-alpine

WORKDIR /app

# Копируем pom.xml для установки зависимостей
COPY pom.xml .

# Загружаем зависимости без сборки
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src /app/src

# Выполняем сборку проекта и создаем JAR-файл
RUN mvn clean package -DskipTests

# Открываем порт для приложения
EXPOSE 8080

# Указываем команду для запуска JAR-файла
CMD ["java", "-jar", "target/WalletService-1.0-SNAPSHOT.jar"]