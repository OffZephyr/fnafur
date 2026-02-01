#version 330
#moj_import <minecraft:globals.glsl>

in vec3 Position; // VertexFormats.POSITION gives vec3

out vec2 vUv;

void main() {
    // Position is already in clipspace for fullscreen tri
    gl_Position = vec4(Position.xy, 0.0, 1.0);
    vUv = Position.xy * 0.5 + 0.5;
}