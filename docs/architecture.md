# Architecture

## High-level flow

`Activity -> GLSurfaceView -> Renderer -> Scene -> Entity/Component`

Arcblade uses a lightweight entity/component approach without introducing a
full ECS framework dependency.

## Threading model

- Main thread: lifecycle and UI-level integration
- GL thread: rendering and frame submission
- Optional fixed-step logic thread: deterministic simulation where needed

## Input pipeline

1. Android input events are captured in `GameSurfaceView`.
2. Events are normalized for orientation and viewport scale.
3. Renderer/scene consumes frame-ready input state.

## Asset pipeline

- Static assets in Android resources
- Runtime loading staged by scene lifecycle
- Explicit ownership to avoid leaks and duplicate loads

## Resource lifecycle

- Create GL resources in `onSurfaceCreated`
- Rebuild size-dependent resources in `onSurfaceChanged`
- Release and recreate safely across context loss
