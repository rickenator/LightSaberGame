# Performance

## Frame budget

- Target 60 FPS
- Per-frame budget: 16.6 ms total

## GC avoidance

- Avoid allocations in frame loops
- Reuse arrays and temporary structures
- Precompute where practical

## Profiling tips

- Use Perfetto for frame timeline analysis
- Use Systrace for CPU scheduling and thread contention checks
- Validate frame pacing across representative devices

## Draw-call targets

- Keep draw calls minimal per scene
- Batch static geometry and materials when possible
- Track draw-call changes during feature development
