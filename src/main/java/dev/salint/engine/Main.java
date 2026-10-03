package dev.salint.engine;

import dev.salint.engine.graphics.*;
import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.Transform;
import dev.salint.engine.window.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;


public class Main {
    static void main(String[] args) {

        Window window = new Window(800, 500, "Game Engine");

        double lastTime = GLFW.glfwGetTime();

        float[] vertices = {
                // position          // UV
                -0.5f, -0.5f, 0.0f,  0.0f, 0.0f,
                0.5f, -0.5f, 0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, 0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, 0.0f,  0.0f, 1.0f
        };

        int[] indices = {
                0, 1, 2,
                0, 2, 3
        };

        Mesh triangle = new Mesh(vertices, indices);

        String vertexShaderSource = """
        #version 330 core

        layout (location = 0) in vec3 position;
        layout (location = 1) in vec2 texCoords;
        
        uniform mat4 model;
        uniform mat4 view;
        uniform mat4 projection;
    
        out vec2 uv;

        void main()
        {
            gl_Position = projection * view * model * vec4(position, 1.0);
            
            uv = texCoords;
        }
        """;

        String fragmentShaderSource = """
        #version 330 core

        in vec2 uv;
        out vec4 color;
        
        uniform sampler2D textureSampler;

        void main()
        {
            color = texture(textureSampler, uv);
        }
        """;

        Camera camera = new Camera();

        camera.position.set(0.0f, 0.0f, 2.0f);

        Transform transform = new Transform();
        transform.position.set(0.2f, 0.0f, -2.0f);
        transform.scale.set(1.5f, 1.5f, 1.5f);

        Transform transform2 = new Transform();
        transform2.position.set(-1.0f, 0.0f, -2.0f);
        transform2.scale.set(0.75f, 0.75f, 0.75f);

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

        Renderer renderer = new Renderer();

        while(!window.shouldClose()) {

            double currentTime = GLFW.glfwGetTime();
            float deltaTime = (float) (currentTime - lastTime);
            lastTime = currentTime;

            renderer.begin();

            transform.rotation.y += 1.0f * deltaTime;
            transform.rotation.x += 0.5f * deltaTime;

            transform2.rotation.y += 0.5f * deltaTime;
            transform2.rotation.x += 1.0f * deltaTime;

            renderer.render(triangle, material , transform, camera, projection);

            renderer.render(
                    triangle,
                    material,
                    transform2,
                    camera,
                    projection
            );

            window.update();
        }

        shader.destroy();
        window.destroy();
        triangle.destroy();
    }
}