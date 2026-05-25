# Contributing to Arcblade

Thanks for helping improve Arcblade.

## Development model

- Trunk-based development
- Short-lived branches
- Conventional Commits

## Branch guidance

Use prefixes such as `feat/`, `fix/`, `docs/`, `chore/`, `perf/`, and `test/`.

## Commit format

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```text
<type>(<scope>): <description>
```

## Pull request checklist

- [ ] `./gradlew lint`
- [ ] `./gradlew assembleDebug`
- [ ] `./gradlew testDebugUnitTest`
- [ ] Documentation updated when behavior/design changes
- [ ] `CHANGELOG.md` updated for user-visible changes
- [ ] No new per-frame allocations in rendering paths

## Testing instructions

Run:

```bash
./gradlew lint assembleDebug testDebugUnitTest
```

If the Gradle wrapper jar is missing, regenerate the wrapper first:

```bash
gradle wrapper --gradle-version 8.7
```

## ADR process

Architecture decisions are tracked in `docs/adr/`.

1. Copy the ADR template/process from `docs/adr/README.md`.
2. Assign the next ADR number.
3. Submit the ADR in a PR before implementation when possible.
