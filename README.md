🚀 Flexible API‑Driven Data Storage With Dynamic Validation
Your platform provides a schema‑agnostic, API‑driven storage engine that allows users to save and retrieve any data structure, similar to a document database. It offers the freedom of NoSQL with the control of structured validation.
Developers can define custom types, specify mandatory fields, and register validators that the system automatically discovers and executes based on the object’s declared type. This enables dynamic, on‑the‑fly validation without rigid schemas or migrations.
The result is a powerful, flexible storage solution that adapts to any data model while still enforcing business rules and ensuring data consistency.

✨ Key Features
🔧 Store Any Data Structure
- Accepts arbitrary JSON‑like objects.
- No predefined schema required.
- Ideal for rapidly evolving data models.
  🧠 Dynamic, Type‑Based Validation
- Validators are automatically picked up at runtime.
- Executed based on the object’s declared type.
- Supports custom business rules and mandatory fields.
  🔍 Query by Any Field
- Search across any property of any stored object.
- No need to define indexes or schemas upfront.
  🛡 Controlled Flexibility
- Combine schema‑less storage with optional structure.
- Enforce required fields only when needed.
- Maintain consistency without sacrificing agility.
  ⚡ Zero‑Migration Evolution
- Change data structures freely.
- Add or remove fields without downtime.
- Perfect for fast‑moving projects and prototypes.

🧩 Ideal Use Cases
- Applications with frequently changing data models
- Systems that need flexible storage but still require validation
- Multi‑tenant or plugin‑based platforms with custom data types
- Rapid prototyping and dynamic form builders

manual docker image build
docker build -f Dockerfile-NonNative -t ricardomaximino/flex:latest --build-arg NAME=target/flex-1.0.0 .
docker push ricardomaximino/flex:latest

Optmized to create native image

argument for the agent
-agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image

how to build the native image once the system is already configured
mvn -Pnative native:build -DskipTests
mvn -Pnative native:build -DskipTests package -X

result on windows
.\target\flex.exe

how to run with an specific profile
.\target\flex.exe --spring.profiles.active=standalone

build docker image with a native image (Linux/WSL)

mvn -Pnative native:build -DskipTests

docker build -t ricardomaximino/flex:native .
docker push ricardomaximino/flex:latest
