#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;
uniform sampler2D Sampler4;
uniform sampler2D Sampler5;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
#ifdef PER_FACE_LIGHTING
in vec4 vertexPerFaceColorBack;
in vec4 vertexPerFaceColorFront;
#else
in vec4 vertexColor;
#endif
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

in vec3 N;
in vec3 viewPos;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    vec4 color2 = texture(Sampler3, texCoord0);
    #ifdef NO_EYES

    color2 = vec4(0);
    #endif
    vec4 color3 = texture(Sampler4, texCoord0);
    if(color3.rgb != vec3(0, 0, 0)) color = color2;

    #ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
    #endif


    vec4 preLightingColor = color;

#ifdef PER_FACE_LIGHTING
    color *= (gl_FrontFacing ? vertexPerFaceColorFront : vertexPerFaceColorBack) * ColorModulator;
#else
    color *= vertexColor * ColorModulator;
#endif

    #ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
#endif
    #ifndef EMISSIVE
    color *= lightMapColor;
#endif


    #ifdef GLOWING_IRISES
    if(color3.rgb == vec3(1, 0, 0) || color3.rgb == vec3(0, 0, 1)) {
        vec4 lightColor = texture(Sampler5, texCoord0);
        color = mix(preLightingColor, (preLightingColor * lightColor), color3.a);
        if(lightColor == vec4(1)) {
            color = preLightingColor;
        }
    }
    #endif

    #ifdef GLOWING_EYES
    if(color3.rgb != vec3(0, 0, 0) && color3.rgb != vec3(1, 1, 1)) {
        vec4 lightColor = texture(Sampler5, texCoord0);
        color = mix(preLightingColor, (preLightingColor * lightColor), color3.a);
        if(lightColor == vec4(1)) {
            color = preLightingColor;
        }
    }
    #endif

    #ifdef DOTS
    if(color3.rgb == vec3(0, 0, 1)) color = texture(Sampler5, texCoord0);

    #endif

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
