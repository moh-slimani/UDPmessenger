FROM openjdk:8-jdk-alpine
COPY . /usr/src/
WORKDIR /usr/src/
RUN javac -d out/production/UDPMessenger src/*
CMD ["java","-cp","out/production/UDPMessenger/","UDPClient"]