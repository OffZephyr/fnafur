#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>

layout(std140) uniform LightShadow {
    mat4 LightViewProj;
};

in vec3 Position;

void main() {
    vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
    gl_Position = LightViewProj * vec4(pos, 1.0);
}