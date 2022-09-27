FROM maven:3.8.6-jdk-11
ADD . /
ENV BASE_URL="https://gitlab.com/api/v4"
ENV TOKEN=" "
ENV PROJECTID=""
RUN mvn clean compile
RUN mvn test