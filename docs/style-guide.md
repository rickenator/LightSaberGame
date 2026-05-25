# Style Guide

## Baseline

- Follow Google Java Style conventions
- Keep code focused, readable, and testable

## Package layout

All Android source lives under:

`com.aniviza.arcblade`

Suggested structure:

- `com.aniviza.arcblade` for app entry points
- `com.aniviza.arcblade.util` for narrow utility helpers

## Naming

- Types: `UpperCamelCase`
- Methods/fields: `lowerCamelCase`
- Constants: `UPPER_SNAKE_CASE`

## Performance rules

- Avoid per-frame allocations in rendering loops
- Reuse buffers and temporary objects
- Prefer primitive arrays in hot paths
