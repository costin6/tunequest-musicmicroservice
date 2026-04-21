FROM gradle:8.3-jdk17
WORKDIR /opt/app
COPY ./build/libs/TuneQuestBackend-MusicMicroservice-1.0-SNAPSHOT.jar ./
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar TuneQuestBackend-MusicMicroservice-1.0-SNAPSHOT.jar"]