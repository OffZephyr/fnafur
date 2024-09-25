#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;
uniform float Saturation;
uniform float BulgeIntensity;
uniform float NoiseIntensity;
uniform float scanLineIntensity;
uniform float Time;

out vec4 fragColor;

vec2 bulge(vec2 r, float alpha)
{
    return r * -alpha * (1.0 - dot(r, r));

}

void main() {

    vec2 uv = texCoord;
    uv = (uv - 0.5) * 2.0;

    vec2 fishuv;
    float fishyness = (0.1 + 0.1 * cos(0)) * BulgeIntensity;
    fishuv.x = (1.0 - uv.y*uv.y) * fishyness * uv.x;
    fishuv.y = (1.0 - uv.x*uv.x) * fishyness * uv.y;

    fishuv.x -= 1.0;
    fishuv.y -= 1.0;

    vec2 finale = (uv - fishuv) /2;

    vec4 InTexel = texture(DiffuseSampler, finale);

    // Color Matrix
    float RedValue = dot(InTexel.rgb, RedMatrix);
    float GreenValue = dot(InTexel.rgb, GreenMatrix);
    float BlueValue = dot(InTexel.rgb, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Offset & Scale
    OutColor = (OutColor * ColorScale) + Offset;

    // Saturation
    float Luma = dot(OutColor, Gray);
    vec3 Chroma = OutColor - Luma;
    OutColor = (Chroma * Saturation) + Luma;

    // Apply SSTV effect
    float scanLine = sin(texCoord.y * 600.0 + Time * 10.0) * scanLineIntensity;
    float noise = (fract(sin(dot(texCoord, vec2(12.9898, 78.233)) * (43758.5453 + Time))) - 0.5) * NoiseIntensity;

    OutColor.rgb += scanLine + noise;

    fragColor = vec4(OutColor, 1.0);
}