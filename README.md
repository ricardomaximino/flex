# 🚀 Flex: Flexible API‑Driven Data Storage With Dynamic Validation

Flex is a schema‑agnostic, API‑driven storage engine that allows you to save and retrieve any data structure. It combines the freedom of NoSQL with the control of structured validation, offering a powerful solution for rapidly evolving data models.

## ✨ Key Features

- **🔧 Schema-Agnostic Storage**: Store arbitrary JSON‑like objects without predefined schemas or migrations.
- **🧠 Dynamic, Type‑Based Validation**: Automatically discover and execute validators based on the object's declared `type`.
- **🔍 Advanced Query Engine**: Search across any property with support for operators (`EQUALS`, `CONTAINS`, `GREATER_THAN`, `LESS_THAN`, `BETWEEN`, `IN`, `EXISTS`).
- **📑 Paginated Results**: Built-in support for pagination and sorting across all search endpoints.
- **⚡ High Performance**: Optimized for speed, with support for GraalVM Native Image for lightning-fast startup and low memory footprint.
- **🔌 Multi-Storage Support**: Switch between embedded MapDB for standalone use and MongoDB for production environments.

---

## 🏗 Architecture & Profiles

Flex supports different operational modes via Spring Profiles:

### 🏠 Standalone Profile (`standalone`)
- **Storage**: Uses [MapDB](https://mapdb.org/), an embedded database engine.
- **Ideal for**: Local development, CI/CD pipelines, and lightweight standalone deployments.
- **No Dependencies**: Does not require any external database or message broker.

### 🌐 Production Profile (`prod`)
- **Storage**: Uses **MongoDB** for persistent, scalable storage.
- **Messaging**: Integrates with **RabbitMQ** for event-driven notifications when data is created.
- **Ideal for**: Scalable production environments and microservices architectures.

---

## 🛠 Developer Guide

### 🧬 Adding a New Data Type & Validator
To add a new data type (e.g., `ORDER`), simply create a class that implements the `Validator` interface:

```java
@Component
public class OrderValidator implements Validator {
    @Override
    public String name() { return "ORDER"; }

    @Override
    public void validate(Flex flex) {
        Data data = (Data) flex;
        if (!data.getCustomFields().containsKey("orderId")) {
            throw new ValidationException("orderId is mandatory");
        }
    }
}
```
The `ValidatorManager` will automatically pick up your validator and apply it to any data submitted with `type: "ORDER"`.

### 🔍 Search API Examples

#### Simple Search
`POST /api/data/search`
```json
{
  "type": "CUSTOMER",
  "status": "ACTIVE"
}
```

#### Advanced Search
`POST /api/data/advanced-search`
```json
{
  "age": { "operation": "GREATER_THAN", "value": 18 },
  "country": { "operation": "IN", "value": ["USA", "Canada"] }
}
```

---

## 📦 Build & Deployment

### Manual Docker Build (Standard JVM)
```bash
mvn clean package -DskipTests
docker build -f Dockerfile-NonNative -t flex:latest --build-arg NAME=target/flex-1.0.0 .
```

### Native Image Build (GraalVM)
Flex is optimized for GraalVM Native Image.

1. **Generate Configuration** (Optional, if changes made):
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="-agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image"
   ```

2. **Build Native Executable**:
   ```bash
   mvn -Pnative native:build -DskipTests
   ```

3. **Run Native Executable**:
   ```bash
   # Standalone mode
   ./target/flex --spring.profiles.active=standalone
   
   # Production mode
   ./target/flex --spring.profiles.active=prod
   ```

### Docker Build (Native)
```bash
# Requires GraalVM environment or multi-stage Docker build
mvn -Pnative native:build -DskipTests
docker build -t flex:native .
```

---

## 🧩 Ideal Use Cases
- **Rapid Prototyping**: Iterate on data models without database migrations.
- **Multi-tenant Platforms**: Store custom fields for different users/clients.
- **Dynamic Form Builders**: Save form submissions with varying structures.
- **Microservices**: Use as a flexible metadata store or "document sidecar".

