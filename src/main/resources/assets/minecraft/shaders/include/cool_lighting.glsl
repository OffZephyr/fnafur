#version 330

#ifndef MAX_LIGHTS
#define MAX_LIGHTS 128
#endif

#ifndef MAX_SHADOWED_LIGHTS
#define MAX_SHADOWED_LIGHTS 32
#endif

#ifndef SHADOW_RES
#define SHADOW_RES 1024
#endif

struct AreaLight {
    vec4 pos_radius;
    vec4 dir_length;
    vec4 color_intensity;
    vec4 normal_influence;

// x=edge smoothness, y=distance falloff, z=projectionRadiusStart, w=shadowIndex (-1 or 0..7)
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