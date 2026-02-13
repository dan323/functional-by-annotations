# Building

## Prerequisites

- Java 24+ (project targets Java 24)
- Maven 3.6+

## Build All Modules

```bash
mvn clean
mvn -B -f ./functional-definitions/annotation-definitions/pom.xml package -Dmaven.test.skip=true
mvn install
```

## Run Tests

```bash
mvn clean
mvn -B -f ./functional-definitions/annotation-definitions/pom.xml package -Dmaven.test.skip=true
mvn test -P test
```

## Coverage (JaCoCo)

```bash
mvn clean
mvn -B -f ./functional-definitions/annotation-definitions/pom.xml package -Dmaven.test.skip=true
mvn test -Ptest
mvn verify -Pjacoco
```

## Mutation Testing (Pitest)

```bash
mvn clean
mvn -B -f ./functional-definitions/annotation-definitions/pom.xml package -Dmaven.test.skip=true
mvn test pitest:mutationCoverage
```

## Module Notes

- `functional-definitions` builds the annotations and compiler.
- `example-functional` runs examples and tests that use generated code.
- `jacoco-functional` aggregates coverage for reports.

## Troubleshooting

- If annotation processing is not running in your IDE, ensure annotation processing is enabled.
- If module errors appear, verify that `module-info.java` is recognized by the IDE.
- For pipeline behavior, see `docs/PIPELINE.md`.

