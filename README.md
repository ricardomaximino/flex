# flex
Flex is a software framework designed for easy customization of both its data handling and it's behaviour
docker build -t ricardomaximino/flex:latest .
docker push ricardomaximino/flex:latest

-agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image
.\target\flex.exe
.\target\flex.exe --spring.profiles.active=prod

mvn -Pnative native:build -DskipTests
mvn -Pnative native:build -DskipTests package -X