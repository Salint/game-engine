#version 330 core

in vec2 uv;
in vec3 vertexNormal;
out vec4 color;

uniform sampler2D textureSampler;
uniform vec3 lightDirection;
uniform vec3 lightColor;

void main()
{
    vec3 normal = normalize(vertexNormal);
    vec3 light = normalize(-lightDirection);

    float intensity = max(dot(normal, light), 0.2);

    vec4 textureColor = texture(textureSampler, uv);

    color = textureColor * vec4(lightColor * intensity, 1.0);
}