FROM maven:3.9.6-amazoncorretto-21
WORKDIR /boot/target
COPY /boot/target/relation-service-0.0.1-SNAPSHOT.jar relation-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "relation-service-0.0.1-SNAPSHOT.jar"]