package dev.salint.engine;

import dev.salint.engine.graphics.*;
import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.GameObject;
import dev.salint.engine.scene.Light;
import dev.salint.engine.scene.Scene;
import dev.salint.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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

        Camera camera = new Camera();

        camera.position.set(0.0f, 0.0f, 2.0f);

        Matrix4f projection = new Matrix4f()
                .perspective(
                        (float) Math.toRadians(60.0f),
                        800.0f / 500.0f,
                        0.1f,
                        100.0f
                );

        Shader shader = new Shader("/shaders/basic/vertex.glsl", "/shaders/basic/fragment.glsl");

        Texture texture = new Texture("/textures/test.png");
        texture.bind();

        Material material = new Material(shader, texture);

        GameObject cube = new GameObject();
        cube.transform.position.z = -2f;
        cube.meshRenderer = new MeshRenderer(triangle, material);

        GameObject cube2 = new GameObject();
        cube2.transform.position.z = -4f;
        cube2.transform.position.x = 2f;
        cube2.meshRenderer = new MeshRenderer(triangle, material);

        GameObject cube3 = new GameObject();
        cube3.transform.position.z = -4f;
        cube3.transform.position.x = -2f;
        cube3.meshRenderer = new MeshRenderer(triangle, material);

        Scene mainScene = new Scene();
        mainScene.add(cube);
        mainScene.add(cube2);
        mainScene.add(cube3);

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

            Vector3f move = new Vector3f();

            if (window.isKeyDown(GLFW.GLFW_KEY_A)) {
                move.add(camera.getRight().mul(deltaTime));
            }
            else if (window.isKeyDown(GLFW.GLFW_KEY_D)) {
                move.sub(camera.getRight().mul(deltaTime));
            }

            if (window.isKeyDown(GLFW.GLFW_KEY_W)) {
                move.add(camera.getForward().mul(deltaTime));
            }
            else if (window.isKeyDown(GLFW.GLFW_KEY_S)) {
                move.sub(camera.getForward().mul(deltaTime));
            }
            camera.position.add(move);

            if(window.isKeyDown(GLFW.GLFW_KEY_RIGHT)) camera.rotation.y -= 1.0f * deltaTime;
            else if(window.isKeyDown(GLFW.GLFW_KEY_LEFT)) camera.rotation.y += 1.0f * deltaTime;

            renderer.render(mainScene, camera, projection);

            window.update();
        }

        shader.destroy();
        window.destroy();
        triangle.destroy();
    }
}