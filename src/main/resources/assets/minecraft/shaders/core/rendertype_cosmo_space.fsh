#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:matrix.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec4 texProj0;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;

const vec3[] COLORS = vec3[](
    vec3(0.022087, 0.022087, 0.022087),
    vec3(0.011892, 0.011892, 0.011892),
    vec3(0.027636, 0.027636, 0.027636),
    vec3(0.046564, 0.046564, 0.046564),
    vec3(0.064901, 0.064901, 0.064901),
    vec3(0.063761, 0.063761, 0.063761),
    vec3(0.084817, 0.084817, 0.084817),
    vec3(0.097489, 0.097489, 0.097489),
    vec3(0.106152, 0.106152, 0.106152),
    vec3(0.097721, 0.097721, 0.097721),
    vec3(0.133516, 0.133516, 0.133516),
    vec3(0.235792, 0.235792, 0.235792),
    vec3(0.214696, 0.214696, 0.214696),
    vec3(0.321970, 0.321970, 0.321970),
    vec3(0.390010, 0.390010, 0.390010),
    vec3(1, 1, 1)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
    for (int i = 0; i < PORTAL_LAYERS; i++) {
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = apply_fog(vec4(color, 1.0), sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
