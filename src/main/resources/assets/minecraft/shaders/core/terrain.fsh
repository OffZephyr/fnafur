#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:decals.glsl>
#moj_import <minecraft:chunksection.glsl>

#define MAX_DECALS 32

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 vWorldPos;
in vec3 vNormal;

out vec4 fragColor;

vec4 sampleNearest(sampler2D sampler, vec2 uv, vec2 pixelSize, vec2 du, vec2 dv, vec2 texelScreenSize) {
    // Convert our UV back up to texel coordinates and find out how far over we are from the center of each pixel
    vec2 uvTexelCoords = uv / pixelSize;
    vec2 texelCenter = round(uvTexelCoords) - 0.5f;
    vec2 texelOffset = uvTexelCoords - texelCenter;

    // Move our offset closer to the texel center based on texel size on screen
    texelOffset = (texelOffset - 0.5f) * pixelSize / texelScreenSize + 0.5f;
    texelOffset = clamp(texelOffset, 0.0f, 1.0f);

    uv = (texelCenter + texelOffset) * pixelSize;
    return textureGrad(sampler, uv, du, dv);
}


vec4 sampleNearest(sampler2D source, vec2 uv, vec2 pixelSize) {
    vec2 du = dFdx(uv);
    vec2 dv = dFdy(uv);
    vec2 texelScreenSize = sqrt(du * du + dv * dv);
    return sampleNearest(source, uv, pixelSize, du, dv, texelScreenSize);
}

// Rotated Grid Super-Sampling
vec4 sampleRGSS(sampler2D source, vec2 uv, vec2 pixelSize) {
    vec2 du = dFdx(uv);
    vec2 dv = dFdy(uv);

    vec2 texelScreenSize = sqrt(du * du + dv * dv);
    float maxTexelSize = max(texelScreenSize.x, texelScreenSize.y);

    float minPixelSize = min(pixelSize.x, pixelSize.y);

    float transitionStart = minPixelSize * 1.0;
    float transitionEnd = minPixelSize * 2.0;
    float blendFactor = smoothstep(transitionStart, transitionEnd, maxTexelSize);

    float duLength = length(du);
    float dvLength = length(dv);
    float minDerivative = min(duLength, dvLength);
    float maxDerivative = max(duLength, dvLength);

    float effectiveDerivative = sqrt(minDerivative * maxDerivative);

    float mipLevelExact = max(0.0, log2(effectiveDerivative / minPixelSize));

    float mipLevelLow = floor(mipLevelExact);
    float mipLevelHigh = mipLevelLow + 1.0;
    float mipBlend = fract(mipLevelExact);

    const vec2 offsets[4] = vec2[](
    vec2(0.125, 0.375),
    vec2(-0.125, -0.375),
    vec2(0.375, -0.125),
    vec2(-0.375, 0.125)
    );

    vec4 rgssColorLow = vec4(0.0);
    vec4 rgssColorHigh = vec4(0.0);
    for (int i = 0; i < 4; ++i) {
        vec2 sampleUV = uv + offsets[i] * pixelSize;
        rgssColorLow += textureLod(source, sampleUV, mipLevelLow);
        rgssColorHigh += textureLod(source, sampleUV, mipLevelHigh);
    }
    rgssColorLow *= 0.25;
    rgssColorHigh *= 0.25;

    vec4 rgssColor = mix(rgssColorLow, rgssColorHigh, mipBlend);

    vec4 nearestColor = sampleNearest(source, uv, pixelSize, du, dv, texelScreenSize);

    return mix(nearestColor, rgssColor, blendFactor);
}

bool normalsWithin45Degrees(vec3 a, vec3 b)
{
    return dot(normalize(a), normalize(b)) >= 0.64278761;
}

vec4 applyDecalBlend(vec4 baseColor, vec4 decalColor, int blendMode)
{
    if (blendMode == 0)
    {
        return mix(baseColor, decalColor, decalColor.a);
    }
    else if (blendMode == 1)
    {
        return baseColor + decalColor * decalColor.a;
    }
    else if (blendMode == 2)
    {
        vec3 overlay;
        for (int i = 0; i < 3; i++)
        {
            float b = baseColor[i];
            float d = decalColor[i];
            overlay[i] = (b < 0.5)
            ? (2.0 * b * d)
            : (1.0 - 2.0 * (1.0 - b) * (1.0 - d));
        }

        return vec4(mix(baseColor.rgb, overlay, decalColor.a), baseColor.a);
    }

    return baseColor;
}
vec3 getCameraWorldPos() {
    return vec3(CameraBlockPos) + CameraOffset;
}

vec3 getViewDir(vec3 worldPos) {
    return normalize(getCameraWorldPos() - worldPos);
}

vec4 applyDecals(vec4 baseColor){


    vec4 result = baseColor;

    vec3 N = normalize(vNormal);
    vec3 viewDir = getViewDir(vWorldPos);


    const float DECAL_EPSILON = 0.01;
    vec3 biasedWorldPos = vWorldPos - N * DECAL_EPSILON;

    for (int i = 0; i < DecalCount; i++) {
        Decal decal = decals[i];

        float x1 = decal.DecalPos.x - (0.5 * decal.DecalRight.x);
        float y1 = decal.DecalPos.y - (0.5 * decal.DecalRight.y);
        float z1 = decal.DecalPos.z - (0.5 * decal.DecalRight.z);

        float x2 = decal.DecalEndPos.x - (0.5 * decal.DecalRight.x);
        float y2 = decal.DecalEndPos.y - (0.5 * decal.DecalRight.y);
        float z2 = decal.DecalEndPos.z - (0.5 * decal.DecalRight.z);

        if(decal.DecalForward.x < 0) {
            x1 = x1 - decal.DecalForward.x;
            x2 = x2 - decal.DecalForward.x;
        }
        if(decal.DecalForward.y < 0) {
            y1 = y1 - decal.DecalForward.y;
            y2 = y2 - decal.DecalForward.y;
        }
        if(decal.DecalForward.z < 0) {
            z1 = z1 - decal.DecalForward.z;
            z2 = z2 - decal.DecalForward.z;
        }

        vec3 finalPos1 = vec3(x1, y1, z1);
        vec3 finalPos2 = vec3(x2 + 1, y2 + 1, z2 + 1);

        if(
            biasedWorldPos.x >= x1 &&
            biasedWorldPos.y >= y1 &&
            biasedWorldPos.z >= z1 &&
            biasedWorldPos.x <= x2 + 1 &&
            biasedWorldPos.y <= y2 + 1 &&
            biasedWorldPos.z <= z2 + 1
        ){
            vec3 f = normalize(decal.DecalForward);
            vec3 r = normalize(decal.DecalRight);
            vec3 u = normalize(cross(f, r));

            vec3 toFrag = biasedWorldPos - finalPos1;

            // Distance along decal forward axis
            float forwardDist = dot(toFrag, f);

            float decalLength = length(finalPos2 - finalPos1);

            // Reject fragments outside start/end
//            if (forwardDist < 0.0 || forwardDist > decalLength)
//            continue;

            // Reject steep angles (optional but recommended)
            if (!normalsWithin45Degrees(N, f))
            continue;

            float minU = decal.DecalUV.x;
            float minV = decal.DecalUV.y;
            float maxU = decal.DecalUV.z;
            float maxV = decal.DecalUV.w;

            float uSize = maxU - minU;
            float vSize = maxV - minV;

            // World-space projected coordinates
            float uWorld = dot(biasedWorldPos - finalPos1, r);
            float vWorld = dot(biasedWorldPos - finalPos1, u);

            // Repeat scale (world units per tile)
            float tileScale = 1.0; // adjust to taste

            vec2 uv;
            uv.x = fract(uWorld / tileScale);
            uv.y = fract(vWorld / tileScale);

            // Prevent nearest sampling edge snap
            uv = clamp(uv, 0.001, 0.999);

            // Remap into atlas UVs
            uv = mix(decal.DecalUV.xy, decal.DecalUV.zw, uv);

            vec4 decalSample = (UseRgss == 1 ? sampleRGSS(Sampler0, uv, 1.0f / TextureSize) : sampleNearest(Sampler0, uv, 1.0f / TextureSize));

            result = applyDecalBlend(result, decalSample, decal.DecalBlendMode);

//            float ndotv = dot(N, viewDir);
//            if (ndotv > 0.0) {
//                float depthBias = 1e-4 * ndotv;
//            }
        }
    }

    return result;
}

void main() {
    vec4 color = (UseRgss == 1 ? sampleRGSS(Sampler0, texCoord0, 1.0f / TextureSize) : sampleNearest(Sampler0, texCoord0, 1.0f / TextureSize));
    color = applyDecals(color);
    color = color * vertexColor;
    color = mix(FogColor * vec4(1, 1, 1, color.a), color, ChunkVisibility);
#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
#endif
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
