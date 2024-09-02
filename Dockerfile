FROM openjdk:17 as builder

WORKDIR /app
COPY build/libs/*.jar socialnetwork-0.0.1-SNAPSHOT.jar

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/socialnetwork-0.0.1-SNAPSHOT.jar socialnetwork-0.0.1-SNAPSHOT.jar
EXPOSE 8080
EXPOSE 5432
ENTRYPOINT ["java","-jar","/app/socialnetwork-0.0.1-SNAPSHOT.jar"]