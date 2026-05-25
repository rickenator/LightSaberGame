# ⚔️ Lightsaber Game

> A fast, responsive single-player Android lightsaber action game — built with native Java and OpenGL ES 2.0.

[![CI](https://github.com/rickenator/LightSaberGame/actions/workflows/ci.yml/badge.svg)](https://github.com/rickenator/LightSaberGame/actions/workflows/ci.yml)
[![CodeQL](https://github.com/rickenator/LightSaberGame/actions/workflows/codeql.yml/badge.svg)](https://github.com/rickenator/LightSaberGame/actions/workflows/codeql.yml)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

---

## Table of Contents

1. [Vision](#vision)
2. [Features](#features)
3. [Screenshots](#screenshots)
4. [Tech Stack](#tech-stack)
5. [How to Build](#how-to-build)
6. [How to Run](#how-to-run)
7. [Repository Layout](#repository-layout)
8. [Contributing](#contributing)
9. [Roadmap](#roadmap)
10. [License](#license)

---

## Vision

Lightsaber Game is a **performance-first, pure-Android action game** that puts a lightsaber in your hand via touch and tilt controls. No third-party game engines, no middleware fat — just the Android platform, OpenGL ES 2.0, and carefully written Java.

See [`docs/vision.md`](docs/vision.md) for the full vision statement.

---

## Features

### Current (M0 — Scaffold)
- ✅ Professional repository structure with full documentation
- ✅ Apache-2.0 license
- ✅ CI via GitHub Actions (lint + build + tests)
- ✅ CodeQL security scanning
- ✅ Minimal Android app skeleton with `GLSurfaceView` (deep-space clear color)

### Planned
- 🔜 **M1** — Textured saber quad tracking touch input + swing sound
- 🔜 **M2** — Training-remote enemy, collision detection, scoring, fail state
- 🔜 **M3** — 3 enemy types, level select, settings, persistence
- 🔜 **M4** — Haptics, accessibility, 60fps performance pass
- 🔜 **M5** — Play Store release

See [`docs/roadmap.md`](docs/roadmap.md) for the full milestone breakdown.

---

## Screenshots

> _Screenshots will be added in M1 once there is something to render._

---

## Tech Stack

| Layer | Choice |
|---|---|
| Platform | Android (minSdk 24, targetSdk 34) |
| Language | Java 17 |
| Rendering | OpenGL ES 2.0 via `GLSurfaceView` |
| Build | Gradle 8.7 + Android Gradle Plugin 8.5.2 |
| Tests | JUnit 4 |
| CI | GitHub Actions |

---

## How to Build

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer, **or** JDK 17 + Android SDK (command-line tools)
- Android SDK with `compileSdk 34` platform installed

### Command-line build

```bash
# Clone
git clone https://github.com/rickenator/LightSaberGame.git
cd LightSaberGame

# Generate the Gradle wrapper if not present (requires Gradle 8.7 on PATH)
gradle wrapper --gradle-version=8.7

# Assemble debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Lint
./gradlew lint
```

The debug APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

> **CI note:** The GitHub Actions CI workflow installs Gradle 8.7 explicitly using
> `gradle/actions/setup-gradle` and invokes `./gradlew` after generating the wrapper.
> See `.github/workflows/ci.yml` for details.

---

## How to Run

### Android Studio
1. Open the project root in Android Studio.
2. Select a device or emulator (API 24+).
3. Click **Run ▶**.

### ADB (command-line)
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.rickenator.lightsaber/.MainActivity
```

---

## Repository Layout

```
LightSaberGame/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rickenator/lightsaber/   # Game source
│   │   │   ├── res/                              # Android resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/                                 # Unit tests
│   ├── build.gradle
│   └── proguard-rules.pro
├── docs/                                         # Design & architecture docs
│   ├── adr/                                      # Architecture Decision Records
│   └── ...
├── .github/
│   ├── workflows/                                # CI & CodeQL
│   ├── ISSUE_TEMPLATE/
│   └── PULL_REQUEST_TEMPLATE.md
├── build.gradle                                  # Root build script
├── gradle.properties
├── settings.gradle
├── CHANGELOG.md
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── SECURITY.md
└── LICENSE
```

---

## Contributing

Please read [`CONTRIBUTING.md`](CONTRIBUTING.md) before opening a pull request.  
In short: trunk-based development, short-lived feature branches (`feat/*`, `fix/*`, `docs/*`, `chore/*`), [Conventional Commits](https://www.conventionalcommits.org/).

---

## Roadmap

See [`docs/roadmap.md`](docs/roadmap.md) for milestones M0–M5.

---

## License

Copyright 2026 rickenator  
Licensed under the [Apache License 2.0](LICENSE).
