package dev.salint.engine;

import dev.salint.engine.graphics.*;
import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.GameObject;
import dev.salint.engine.scene.Light;
import dev.salint.engine.scene.Scene;
import dev.salint.engine.window.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;


public class Main {
    static void main(String[] args) {

        Window window = new Window(800, 500, "Game Engine");

        double lastTime = GLFW.glfwGetTime();

        Renderer renderer = new Renderer();

        float[] vertices = {
                // FRONT
                // position          // normal          // UV
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f, 0.0f, 1.0f,

                // BACK
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 1.0f,

                // LEFT
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,

                // RIGHT
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,

                // TOP
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,

                // BOTTOM
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f
        };

        int[] indices = {
                // FRONT
                0,  1,  2,
                0,  2,  3,

                // BACK
                4,  5,  6,
                4,  6,  7,

                // LEFT
                8,  9, 10,
                8, 10, 11,

                // RIGHT
                12, 13, 14,
                12, 14, 15,

                // TOP
                16, 17, 18,
                16, 18, 19,

                // BOTTOM
                20, 21, 22,
                20, 22, 23
        };

        Mesh triangle = new Mesh(vertices, indices);

        String vertexShaderSource = """
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
        """;

        String fragmentShaderSource = """
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
        """;

        Camera camera = new Camera();

        camera.position.set(0.0f, 0.0f, 2.0f);

        Matrix4f projection = new Matrix4f()
                .perspective(
                        (float) Math.toRadians(60.0f),
                        800.0f / 500.0f,
                        0.1f,
                        100.0f
                );

        Shader shader = new Shader(vertexShaderSource, fragmentShaderSource);

        Texture texture = new Texture("/textures/test.png");
        texture.bind();

        Material material = new Material(shader, texture);

        GameObject cube = new GameObject();
        cube.transform.position.z = -2f;
        cube.meshRenderer = new MeshRenderer(triangle, material);

        Scene mainScene = new Scene();
        mainScene.add(cube);

        Light sun = new Light();
        sun.direction.set(
                -1.0f,
                -1.0f,
                -1.0f
        );
        mainScene.add(sun);

        while(!window.shouldClose()) {

            double currentTime = GLFW.glfwGetTime();
            float deltaTime = (float) (currentTime - lastTime);
            lastTime = currentTime;

            renderer.begin();

            cube.transform.rotation.y += 1.0f * deltaTime;

            renderer.render(mainScene, camera, projection);

            window.update();
        }

        shader.destroy();
        window.destroy();
        triangle.destroy();
    }
}