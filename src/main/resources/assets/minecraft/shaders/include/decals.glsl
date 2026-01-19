#version 330

#define MAX_DECALS 512

struct Decal {
    vec3 DecalPos;        float _pad0;
    vec3 DecalEndPos;     float _pad1;
    vec3 DecalForward;    float _pad2;
    vec3 DecalRight;      float _pad3;
    vec4 DecalUV;
    int  DecalBlendMode;  ivec3 _pad5;
};

layout(std140) uniform DecalInfo {
    int DecalCount;
    ivec3 _padHeader;
    Decal decals[MAX_DECALS];
};