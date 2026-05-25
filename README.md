# Arcblade

> A fast, focused Android energy-blade action game.

[![CI](#)](#)
[![CodeQL](#)](#)

## Table of Contents

- [Vision](#vision)
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech Stack](#tech-stack)
- [Build](#build)
- [Run](#run)
- [Repository Layout](#repository-layout)
- [Contributing](#contributing)
- [Roadmap](#roadmap)
- [License](#license)

## Vision

Arcblade is a fast, responsive, single-player Android energy-blade action game built directly on native Android APIs and OpenGL ES 2.0.

The GitHub repository slug remains `LightSaberGame` for legacy reasons, while the product name is Arcblade.

## Features

### Current (M0)
- Project scaffold with guiding docs and ADRs
- Apache-2.0 licensing and contributor guidance
- CI, CodeQL, and Dependabot setup
- Minimal Android + OpenGL ES 2.0 application skeleton

### Planned
- M1: Touch-following blade prototype with swing audio
- M2: Training drone encounter with scoring and fail states
- M3: Patrol sentry and duelist opponent encounters
- M4: Performance and accessibility polish
- M5: Release-ready package and store assets

## Screenshots

Screenshots will be added as milestones progress.

## Tech Stack

- Android
- Java 17
- OpenGL ES 2.0
- Gradle

## Build

```bash
./gradlew assembleDebug
```

## Run

- Open in Android Studio and run on a device/emulator, or
- Install APK manually:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.aniviza.arcblade/.MainActivity
```

## Repository Layout

```text
app/                Android app module
docs/               Vision, design, architecture, roadmap, ADRs
.github/            Templates and automation workflows
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Roadmap

See [docs/roadmap.md](docs/roadmap.md).

## License

Apache-2.0. See [LICENSE](LICENSE).
