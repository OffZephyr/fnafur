#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:cool_lighting.glsl>

uniform sampler2D NormalBuffer;
uniform sampler2D DepthBuffer;
uniform sampler2D Sampler0;
uniform sampler2D PositionBuffer;
uniform float PosRange; // same as prepass

uniform sampler2D ShadowSampler0;
uniform sampler2D ShadowSampler1;
uniform sampler2D ShadowSampler2;
uniform sampler2D ShadowSampler3;
uniform sampler2D ShadowSampler4;
uniform sampler2D ShadowSampler5;
uniform sampler2D ShadowSampler6;
uniform sampler2D ShadowSampler7;

in vec2 vUv;
out vec4 fragColor;

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

vec3 decodeNormal(vec4 enc) {
    vec3 n = enc.rgb * 2.0 - 1.0;
    return normalize(n);
}

// Reconstruct world position from depth
vec3 reconstructViewPos(vec2 uv, float depth01) {
    // NDC
    vec4 ndc = vec4(uv * 2.0 - 1.0, depth01 * 2.0 - 1.0, 1.0);

    mat4 invProj = inverse(ProjMat);

    vec4 viewPos = ProjMat * ndc;
    viewPos.xyz /= max(viewPos.w, 1e-6);
    return viewPos.xyz; // view space
}
mat3 invViewRot() {
    // For a pure rotation matrix, inverse == transpose.
    // ModelViewMat is typically world->view rotation (+maybe tiny numerical error)
    return transpose(mat3(ModelViewMat));
}

vec3 getCameraWorldPos() {
    return vec3(CameraBlockPos) - CameraOffset;
//    return vec3(-CameraBlockPos.x + CameraOffset.x, CameraBlockPos.y - CameraOffset.y, -CameraBlockPos.z + CameraOffset.z);
}

vec3 reconstructWorldPos(vec2 uv, float depth01) {
    vec3 viewPos = reconstructViewPos(uv, depth01);
    vec3 worldDelta = invViewRot() * viewPos;  // <-- key line
    return getCameraWorldPos() + viewPos;
}


vec3 decodeWorldPos(vec2 uv) {
    vec3 enc = texture(PositionBuffer, uv).rgb;     // 0..1
    vec3 rel = (enc * 2.0 - 1.0) * PosRange;       // back to camera-relative
    return getCameraWorldPos() + rel;
}

float shadowVisibilityForLight(AreaLight L, vec3 camRelPos, vec3 N) {    int sidx = int(L.smooth_shadowLayer.w);
    if (sidx < 0) return 1.0;

    // camRelPos is already relative to camera
    vec3 relPos = camRelPos;

    // Now ALL of this must be cam-relative too:
    vec3 P = camRelPos;
    vec3 A = L.pos_radius.xyz;     // MUST be cam-relative in UBO
    vec3 dir = normalize(L.dir_length.xyz); // direction is fine either way
    float len = max(L.dir_length.w, 0.0);

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
    float projRadius2 = mix(projRadiusStart, projRadiusEnd, 1 - t);

    vec4 lp = L.lightViewProj * vec4(relPos, 1.0);
    vec3 ndc = lp.xyz / max(lp.w, 1e-6);

    vec3 uvz;
    uvz.xy = ndc.xy * 0.5 + 0.5;
    uvz.z  = ndc.z  * 0.5 + 0.5;

    if (uvz.x < 0.5 - projRadius || uvz.x > 0.5 + projRadius || uvz.y < 0.5 - projRadius || uvz.y > 0.5 + projRadius || uvz.z < 0.5 - projRadius || uvz.z > 0.5 + projRadius)
    return 1.0;

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

vec3 applyAreaLights(vec3 camPos, vec3 N) {
    vec3 result = vec3(0);

    vec3 P = camPos;

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

        float shadowVis = shadowVisibilityForLight(L, P, N);
        shadowVis = 1.0;

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
    vec4 nEnc = texture(NormalBuffer, vUv);
    vec3 N = decodeNormal(nEnc);

    float depth01 = texture(DepthBuffer, vUv).r;
    if (depth01 >= 1.0) {
        fragColor = vec4(1.0); // sky/background -> no lighting
        return;
    }

    vec3 camPos = texture(PositionBuffer, vUv).xyz;

    vec3 lightMul = applyAreaLights(camPos, N);

    fragColor = vec4(lightMul, 1.0);

//    vec3 cam = getCameraWorldPos(); // or whatever you use
//    vec3 rel = (worldPos - cam) / 64.0;     // now roughly -1..1 in nearby space
//
//    fragColor = vec4(rel * 0.5 + 0.5, 1.0); // map -1..1 -> 0..1
//    fragColor = texture(PositionBuffer, vUv);
}