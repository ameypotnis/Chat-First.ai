FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

# Copy the JAR file from the build stage
COPY ./build/libs/chat-first-ai-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the startup command to run the JAR
CMD ["java", "-jar", "/app/app.jar"]