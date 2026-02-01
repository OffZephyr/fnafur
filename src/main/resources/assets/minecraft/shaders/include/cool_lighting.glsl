#version 330

#ifndef MAX_LIGHTS
#define MAX_LIGHTS 128
#endif

struct AreaLight {
    vec4 pos_radius;
    vec4 dir_length;
    vec4 color_intensity;
    vec4 normal_influence;
    vec4 smooth_shadowLayer;
    vec4 proj_uv;
    vec4 shape_uv;
    mat4 lightViewProj;
};

layout(std140) uniform LightData {
    int LightCount;
    ivec3 _padHeader;
    AreaLight Lights[MAX_LIGHTS];
} LightDataUbo;

float saturate(float x) { return clamp(x, 0.0, 1.0); }

float smoothAtten(float x, float smoothness) {
    float s = mix(0.001, 0.5, saturate(smoothness));
    return smoothstep(1.0, 1.0 - s, x);
}

float distanceToSegment(vec3 P, vec3 A, vec3 B) {
    vec3 AB = B - A;
    float t = dot(P - A, AB) / max(dot(AB, AB), 1e-6);
    t = clamp(t, 0.0, 1.0);
    vec3 Q = A + t * AB;
    return length(P - Q);
}

vec2 projectToRectUV(vec2 localXY, vec4 rect) {
    vec2 uv01 = localXY * 0.5 + 0.5;
    return mix(rect.xy, rect.zw, uv01);
}