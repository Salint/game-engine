#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 uv;
out vec3 vertexNormal;

void main()
{
    gl_Position = projection * view * model * vec4(position, 1.0);

    uv = texCoords;
    mat3 normalMatrix = transpose(inverse(mat3(model)));
    vertexNormal = normalize(normalMatrix * normal);
}