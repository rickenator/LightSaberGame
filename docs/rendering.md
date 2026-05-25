# Rendering

Arcblade targets OpenGL ES 2.0 for broad Android compatibility and predictable
performance characteristics.

## Rendering specifics

- GLES 2.0 shaders for programmable pipeline control
- Vertex/index buffers (VBO/IBO) for mesh data
- Draw call batching where practical to reduce driver overhead

## Shader management

- Compile and validate vertex/fragment shaders
- Link and validate programs
- Fail fast with clear logs on shader errors

## Why GLES 2.0

- Stable baseline across many devices
- Sufficient for the initial visual goals
- Lower complexity and dependency footprint

## Adding a mesh

1. Define vertex/index arrays.
2. Upload to VBO/IBO.
3. Bind attributes/uniforms in shader program.
4. Issue draw call in render pass.

## Adding a shader

1. Add source strings/files.
2. Compile with helper utility.
3. Link program and validate.
4. Integrate into render path with uniform/attribute setup.
