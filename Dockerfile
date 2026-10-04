FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    xvfb \
    libgl1 \
    libgtk-3-0 \
    libxtst6 \
    libxi6 \
    libxrender1 \
    libxext6 \
    fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/OTP1_inclass1_assignment_Khaled_Marai-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-Dprism.order=sw", "-jar", "app.jar"]