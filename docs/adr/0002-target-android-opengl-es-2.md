# ADR 0002: Target native Android with OpenGL ES 2.0

- Status: Accepted
- Date: 2026-05-25

## Context

The project needs strong runtime performance with minimal dependency overhead
and broad Android support.

## Decision

Use native Android application code with OpenGL ES 2.0 rendering. Do not adopt
a heavyweight third-party game engine.

## Consequences

- Fine-grained control of render and lifecycle behavior
- Smaller dependency surface
- More direct responsibility for engine-level systems
