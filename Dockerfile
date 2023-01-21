FROM openjdk:8-alpine

COPY target/uberjar/full-stack-app.jar /full-stack-app/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/full-stack-app/app.jar"]
