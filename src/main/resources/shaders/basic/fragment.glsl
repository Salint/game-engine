#version 330 core
#define MAX_LIGHTS 4

in vec2 uv;
in vec3 vertexNormal;
out vec4 color;

uniform sampler2D textureSampler;

struct Light {
    vec3 direction;
    vec3 color;
};

uniform Light lights[MAX_LIGHTS];
uniform int lightCount;

void main()
{
    vec3 normal = normalize(vertexNormal);

    vec3 lighting = vec3(0.0);

    for (int i = 0; i < lightCount; i++)
    {
        vec3 light = normalize(-lights[i].direction);

        float intensity = max(dot(normal, light), 0.3);

        lighting += lights[i].color * intensity;
    }

    vec4 textureColor = texture(textureSampler, uv);

    color = textureColor * vec4(lighting, 1.0);
}