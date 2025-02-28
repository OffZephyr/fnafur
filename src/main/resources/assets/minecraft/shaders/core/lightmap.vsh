#version 150

in vec3 Position;
in vec2 Texture;

out vec2 texCoord;
out vec2 colorCoord;

void main() {
    vec2 screenPos = Position.xy * 2.0 - 1.0;
    gl_Position = vec4(screenPos.x, screenPos.y, 1.0, 1.0);
    texCoord = vec2(Position.x, Position.y);
    colorCoord = Texture;
}
