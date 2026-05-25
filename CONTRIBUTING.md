# Contributing to Lightsaber Game

Thank you for your interest in contributing! This document describes our branching model, commit conventions, and pull request process.

---

## Table of Contents

1. [Branching Model](#branching-model)
2. [Commit Convention](#commit-convention)
3. [Pull Request Checklist](#pull-request-checklist)
4. [How to Run Tests](#how-to-run-tests)
5. [Architecture Decision Records (ADRs)](#architecture-decision-records-adrs)
6. [Code of Conduct](#code-of-conduct)

---

## Branching Model

We use **trunk-based development** with short-lived feature branches.

| Branch prefix | Purpose |
|---|---|
| `feat/*` | New features |
| `fix/*` | Bug fixes |
| `docs/*` | Documentation only |
| `chore/*` | Build tooling, CI, dependencies |
| `refactor/*` | Code restructuring without behavior change |
| `perf/*` | Performance improvements |
| `test/*` | Adding or improving tests |

- Branch from `main`, keep branches short-lived (ideally < 2 days).
- Rebase onto `main` before opening a PR (no merge commits in feature branches).
- Delete branches after merging.

---

## Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).

```
<type>(<optional scope>): <short description>

[optional body]

[optional footer(s)]
```

**Types:** `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `build`, `ci`

**Examples:**
```
feat(renderer): add shader program compilation helper
fix(input): correct touch coordinate mapping for landscape orientation
docs(roadmap): mark M0 as complete
chore(deps): bump AGP to 8.5.2
```

---

## Pull Request Checklist

Before submitting a PR, confirm:

- [ ] Code compiles: `./gradlew assembleDebug`
- [ ] All unit tests pass: `./gradlew testDebugUnitTest`
- [ ] Lint passes: `./gradlew lint`
- [ ] `CHANGELOG.md` updated under `[Unreleased]`
- [ ] Docs updated if public API or architecture changed
- [ ] No new heap allocations in the render loop (if touching rendering code)
- [ ] Branch rebased onto latest `main`

---

## How to Run Tests

### Prerequisites

- JDK 17 on `PATH`
- Android SDK installed (or let CI handle it)
- Gradle wrapper generated (see README "How to Build")

### Unit tests (host JVM — fast)

```bash
./gradlew testDebugUnitTest
```

Test sources live in `app/src/test/java/com/rickenator/lightsaber/`.

### Instrumented tests (requires device/emulator — M1+)

```bash
./gradlew connectedDebugAndroidTest
```

---

## Architecture Decision Records (ADRs)

Significant technical decisions are recorded as ADRs in `docs/adr/`.

**To propose a new ADR:**

1. Copy `docs/adr/0001-record-architecture-decisions.md` as a template.
2. Assign the next sequential number.
3. Fill in Context, Decision, and Consequences.
4. Open a PR with the `docs/*` prefix for discussion before implementation.

See [`docs/adr/README.md`](docs/adr/README.md) for the full process.

---

## Code of Conduct

This project follows the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating you agree to uphold its terms.
