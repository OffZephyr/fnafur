#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:fog.glsl>

in vec3 vNormal;
out vec4 fragColor;

void main() {
    vec3 N = normalize(vNormal);
    // encode [-1..1] -> [0..1]
    fragColor = vec4(N * 0.5 + 0.5, 1.0);
}