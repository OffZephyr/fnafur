#version 330
#moj_import <minecraft:globals.glsl>


in vec3 vCamRelPos;
in vec3 vWorldPos;
out vec4 fragColor;

// Must match what you use on CPU.
// Use a constant first (no uniform system required).
#define POS_RANGE 128.0

vec3 getCameraWorldPos() {
    return vec3(CameraBlockPos) + CameraOffset;
}

void main() {
    fragColor = vec4(vCamRelPos, 1.0);
}