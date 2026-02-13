# Contributing

## Read before contributing
We are happy to receive not only requests, but also contributions. 😆
If you want to contribute, but you have no idea what to add; take a look at the issues opened
and ask in the thread of that issue if you may contribute within that issue. Better to start
with issues marked as `good first issue` and/or `help wanted`.

When contributing try to follow the ideas already in the sourcecode; but this rule is not written in
stone. Nevertheless, it will require a proper discussion to change the way things are done until now. If you feel
a deep change is worth it, try to make your point across, and it will be considered. The recommendation
is still to have an understanding on the project before trying to refactor the whole thing. 🧑‍🔧

Every change should be done through a **PR** describing what the change actually is, accompanied
by tests. This project executes mutation testing, and we will like to maintain **mutation coverage**
beyond *85%* at all time; hence the importance of testing all possible options.

## Contribution Workflow

1. Open or select an issue to discuss the change.
2. Create a branch from `master`.
3. Keep changes focused and include tests.
4. Open a PR describing the change, motivation, and testing done.

## Adding a New Structure or Annotation

1. Add the annotation and interface in `functional-definitions/annotation-definitions`.
2. Define minimal method requirements in the compiler (see `StructureSignatures`).
3. Update dependency ordering in `FunctionalDependencyTree` if applicable.
4. Add examples in `example-functional`.
5. Add tests for the compiler and the example structure.
6. Update docs in `docs/` and the root `README.md`.

## Modifying the Compiler

- Keep changes small and tested.
- Add or update tests under `functional-definitions/functional-compiler/src/test/java`.
- Validate generated code by running tests in `example-functional`.

## Testing Requirements

All contributions must maintain our quality standards:

- ✅ **85%+ code coverage** (JaCoCo)
- ✅ **85%+ mutation coverage** (Pitest)
- ✅ **Pass SonarCloud quality gate**
- ✅ **Zero bugs and vulnerabilities**

### Running Tests Locally

```bash
# Unit tests
mvn clean test

# With coverage
mvn clean test -Ptest
mvn jacoco:report -Pjacoco

# Mutation testing
mvn clean test pitest:mutationCoverage
```

## Releasing

1. Ensure all tests and quality gates pass.
2. Update `CHANGELOG.md`.
3. Tag the release and run the release workflow.

See `docs/PIPELINE.md` for CI/CD details.

**See**: [Pipeline Documentation](docs/PIPELINE.md) for detailed testing information and troubleshooting.
