#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:decals.glsl>
#moj_import <minecraft:cool_lighting.glsl>
#moj_import <minecraft:chunksection.glsl>


uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform sampler2D ShadowSampler0;
uniform sampler2D ShadowSampler1;
uniform sampler2D ShadowSampler2;
uniform sampler2D ShadowSampler3;
uniform sampler2D ShadowSampler4;
uniform sampler2D ShadowSampler5;
uniform sampler2D ShadowSampler6;
uniform sampler2D ShadowSampler7;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 vWorldPos;
in vec3 vNormal;

out vec4 fragColor;

vec4 sampleNearest(sampler2D sampler, vec2 uv, vec2 pixelSize, vec2 du, vec2 dv, vec2 texelScreenSize) {
    vec2 uvTexelCoords = uv / pixelSize;
    vec2 texelCenter = round(uvTexelCoords) - 0.5f;
    vec2 texelOffset = uvTexelCoords - texelCenter;

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

bool normalsWithinDegrees(vec3 a, vec3 b, float degrees)
{
    float cosThreshold = cos(radians(degrees));
    return dot(normalize(a), normalize(b)) >= cosThreshold;
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

    const float DECAL_EPSILON = 0.001;
    vec3 biasedWorldPos = vWorldPos - N * DECAL_EPSILON;

    for (int i = 0; i < DecalCount; i++) {
        Decal decal = decals[i];

        float x1 = decal.DecalPos.x - (0.5 * decal.DecalRight.x);
        float y1 = decal.DecalPos.y - (0.5 * decal.DecalRight.y);
        float z1 = decal.DecalPos.z - (0.5 * decal.DecalRight.z);

        float x2 = decal.DecalEndPos.x - (0.5 * decal.DecalRight.x);
        float y2 = decal.DecalEndPos.y - (0.5 * decal.DecalRight.y);
        float z2 = decal.DecalEndPos.z - (0.5 * decal.DecalRight.z);

        if(decal.DecalForward.x < 0) { x1 = x1 - decal.DecalForward.x; x2 = x2 - decal.DecalForward.x; }
        if(decal.DecalForward.y < 0) { y1 = y1 - decal.DecalForward.y; y2 = y2 - decal.DecalForward.y; }
        if(decal.DecalForward.z < 0) { z1 = z1 - decal.DecalForward.z; z2 = z2 - decal.DecalForward.z; }

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

            if (!normalsWithinDegrees(N, f, 50.0))
            continue;

            float tileScale = 1.0;

            float uWorld = dot(biasedWorldPos - finalPos1, r);
            float vWorld = dot(biasedWorldPos - finalPos1, u);

            vec2 uv;
            uv.x = fract(uWorld / tileScale);
            uv.y = fract(vWorld / tileScale);
            uv = clamp(uv, 0.001, 0.999);
            uv = mix(decal.DecalUV.xy, decal.DecalUV.zw, uv);

            vec4 decalSample = (UseRgss == 1 ? sampleRGSS(Sampler0, uv, 1.0f / TextureSize) : sampleNearest(Sampler0, uv, 1.0f / TextureSize));
            result = applyDecalBlend(result, decalSample, decal.DecalBlendMode);
        }
    }

    return result;
}

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

float sampleShadowDepth(int idx, vec2 uv) {
    if (idx == 0) return texture(ShadowSampler0, uv).r;
    if (idx == 1) return texture(ShadowSampler1, uv).r;
    if (idx == 2) return texture(ShadowSampler2, uv).r;
    if (idx == 3) return texture(ShadowSampler3, uv).r;
    if (idx == 4) return texture(ShadowSampler4, uv).r;
    if (idx == 5) return texture(ShadowSampler5, uv).r;
    if (idx == 6) return texture(ShadowSampler6, uv).r;
    return texture(ShadowSampler7, uv).r;
}

float shadowVisibilityForLight(AreaLight L, vec3 worldPos) {
    int sidx = int(L.smooth_shadowLayer.w);
    if (sidx < 0) return 1.0;


    vec3 camWorld = vec3(CameraBlockPos) - CameraOffset;
    vec3 relPos = worldPos - camWorld;

    vec4 lp = L.lightViewProj * vec4(relPos, 1.0);
    vec3 ndc = lp.xyz / max(lp.w, 1e-6);

    vec3 uvz;
    uvz.xy = ndc.xy * 0.5 + 0.5;
    uvz.z  = ndc.z  * 0.5 + 0.5;

    if (uvz.x < 0.0 || uvz.x > 1.0 || uvz.y < 0.0 || uvz.y > 1.0 || uvz.z < 0.0 || uvz.z > 1.0)
    return 1.0;

    vec3 N = normalize(vNormal);

    vec3 A = L.pos_radius.xyz;
    vec3 dir = normalize(L.dir_length.xyz);
    float len = max(L.dir_length.w, 0.0);


    vec3 P = vWorldPos;

    vec3 AP = P - A;
    float t = (len > 1e-6) ? clamp(dot(AP, dir) / len, 0.0, 1.0) : 0.0;
    vec3 axisPoint = A + dir * (t * len);

    vec3 radial = P - axisPoint;
    float radialLen = length(radial);
    vec3 radialDir = (radialLen > 1e-6) ? (radial / radialLen) : vec3(0.0);

    float d = length(radial);

    float r0 = max(L.smooth_shadowLayer.z, 0.001);
    float r1 = max(L.pos_radius.w, 0.001);
    float dr = r1 - r0;

    vec3 upGuess = abs(dir.y) > 0.99 ? vec3(1,0,0) : vec3(0,1,0);
    vec3 right = normalize(cross(upGuess, dir));
    vec3 up = normalize(cross(dir, right));

    float projRadiusStart = max(L.smooth_shadowLayer.z, 0.001);
    float projRadiusEnd = max(L.pos_radius.w, 0.001);
    float projRadius = mix(projRadiusStart, projRadiusEnd, t);
    vec3 rel = P - A;
    float x = dot(rel, right) / projRadius;
    float y = dot(rel, up) / projRadius;
    vec2 projUv = projectToRectUV(vec2(x, y), L.proj_uv);
    if(projUv.x < L.proj_uv.x || projUv.x > L.proj_uv.z || projUv.y < L.proj_uv.y || projUv.y > L.proj_uv.w) return 1.0;

    float slope = (len > 1e-6) ? (dr / len) : 0.0;

    vec3 eff = normalize((-dir) + radialDir * slope);

    float ndotl = clamp(dot(N, eff), 0.0, 1.0);

    float bias = mix(0.0025, 0.0008, ndotl);

    vec2 texel = 1.0 / vec2(float(SHADOW_RES), float(SHADOW_RES));

    float sum = 0.0;
    int count = 0;

    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 o = vec2(x, y) * texel;
            float d = sampleShadowDepth(sidx, uvz.xy + o);
            sum += ((uvz.z - bias) <= d) ? 1.0 : 0.0;
            count++;
        }
    }

    return sum / float(count);
}

vec3 applyAreaLights(vec3 baseRgb) {
    vec3 result = baseRgb;

    vec3 P = vWorldPos;
    vec3 N = normalize(vNormal);

    for (int i = 0; i < LightDataUbo.LightCount; i++) {
        AreaLight L = LightDataUbo.Lights[i];

        vec3 A = L.pos_radius.xyz;
        vec3 dir = normalize(L.dir_length.xyz);
        float lengthL = max(L.dir_length.w, 0.0);
        vec3 B = A + dir * lengthL;

        float radius = max(L.pos_radius.w, 0.001);

        float d = distanceToSegment(P, A, B);

        float radial01 = saturate(1.0 - d / radius);
        float radial = smoothAtten(radial01, L.smooth_shadowLayer.x);

        float distFromStart = length(P - A);
        float dist01 = 1.0 / (1.0 + distFromStart * max(L.smooth_shadowLayer.y, 0.0));

        float normalInfluence = saturate(L.normal_influence.w);
        float facing = saturate(dot(N, normalize(L.normal_influence.xyz)));
        float normalTerm = mix(1.0, facing, normalInfluence);

        vec3 upGuess = abs(dir.y) > 0.99 ? vec3(1,0,0) : vec3(0,1,0);
        vec3 right = normalize(cross(upGuess, dir));
        vec3 up = normalize(cross(dir, right));

        float t = 0.0;
        if (lengthL > 1e-6) {
            t = clamp(dot(P - A, dir) / lengthL, 0.0, 1.0);
        }

        float projRadiusStart = max(L.smooth_shadowLayer.z, 0.001);

        float projRadiusEnd = max(L.pos_radius.w, 0.001);

        float tt = t * t;

        float projRadius = mix(projRadiusStart, projRadiusEnd, t);

        vec3 rel = P - A;
        float x = dot(rel, right) / projRadius;
        float y = dot(rel, up) / projRadius;

        vec2 projUv = projectToRectUV(vec2(x, y), L.proj_uv);
        vec2 shapeUv = projectToRectUV(vec2(x, y), L.shape_uv);

        if(projUv.x < L.proj_uv.x || projUv.x > L.proj_uv.z || projUv.y < L.proj_uv.y || projUv.y > L.proj_uv.w) continue;
        if(shapeUv.x < 0.0 || shapeUv.x > 1.0 || shapeUv.y < 0.0 || shapeUv.y > 1.0) continue;

        vec4 projTex = texture(Sampler0, projUv);
        float shapeMask = texture(Sampler0, shapeUv).r;

        float shadowVis = shadowVisibilityForLight(L, P);

        float intensity = L.color_intensity.w;
        vec3 lightColor = L.color_intensity.rgb;

        float contrib = radial * dist01 * normalTerm * shapeMask * shadowVis;
        vec3 projectedTint = projTex.rgb;

        vec3 res = lightColor * projectedTint * (intensity * contrib);
        res *= 1 - t;
        result += res;

    }

    return result;
}

void main() {
    vec4 base = (UseRgss == 1 ? sampleRGSS(Sampler0, texCoord0, 1.0f / TextureSize) : sampleNearest(Sampler0, texCoord0, 1.0f / TextureSize));

    base = applyDecals(base);
    base *= vertexColor;

    vec3 litRgb = applyAreaLights(base.rgb);

    vec4 outColor = vec4(litRgb, base.a);

    #ifdef ALPHA_CUTOUT
    if (outColor.a < ALPHA_CUTOUT) {
        discard;
    }
    #endif

    outColor = mix(FogColor * vec4(1, 1, 1, outColor.a), outColor, ChunkVisibility);

    fragColor = apply_fog(outColor, sphericalVertexDistance, cylindricalVertexDistance,
    FogEnvironmentalStart, FogEnvironmentalEnd,
    FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}