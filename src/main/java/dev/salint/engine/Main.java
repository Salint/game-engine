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

        Time time = new Time();
        Renderer renderer = new Renderer();

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

        Material material = new Material(shader, texture);

        Mesh cubeMesh = Mesh.cube();

        GameObject cube = new GameObject();
        cube.transform.position.z = -2f;
        cube.meshRenderer = new MeshRenderer(cubeMesh, material);

        GameObject cube2 = new GameObject();
        cube2.transform.position.z = -4f;
        cube2.transform.position.x = 2f;
        cube2.meshRenderer = new MeshRenderer(cubeMesh, material);

        GameObject cube3 = new GameObject();
        cube3.transform.position.z = -4f;
        cube3.transform.position.x = -2f;
        cube3.meshRenderer = new MeshRenderer(cubeMesh, material);

        Texture grassTexture = new Texture("/textures/grass.png");

        Material grassMaterial = new Material(shader, grassTexture);

        Scene mainScene = new Scene();
        mainScene.add(cube);
        mainScene.add(cube2);
        mainScene.add(cube3);

        Mesh plane = Mesh.plane();
        for(int i = -26; i < 50; i++) {
            for(int j = -26; j < 50; j++) {
                GameObject ground = new GameObject();
                ground.transform.position.x = i * 1f;
                ground.transform.position.y = -0.5f;
                ground.transform.position.z = j * 1f;
                ground.meshRenderer = new MeshRenderer(plane, grassMaterial);
                mainScene.add(ground);
            }
        }

        Light sun = new Light();
        sun.direction.set(
                -1.0f,
                -1.0f,
                -1.0f
        );
        mainScene.add(sun);

        while(!window.shouldClose()) {
            renderer.begin();
            time.update();

            Vector3f move = new Vector3f();

            if (window.isKeyDown(GLFW.GLFW_KEY_A)) {
                move.add(camera.getRight().mul(time.getDeltaTime()));
            }
            else if (window.isKeyDown(GLFW.GLFW_KEY_D)) {
                move.sub(camera.getRight().mul(time.getDeltaTime()));
            }

            if (window.isKeyDown(GLFW.GLFW_KEY_W)) {
                move.add(camera.getForward().mul(time.getDeltaTime()));
            }
            else if (window.isKeyDown(GLFW.GLFW_KEY_S)) {
                move.sub(camera.getForward().mul(time.getDeltaTime()));
            }

            if(move.length() > 1) move.normalize();
            camera.position.add(move);

            if(window.isKeyDown(GLFW.GLFW_KEY_RIGHT)) camera.rotation.y -= 1.0f * time.getDeltaTime();
            else if(window.isKeyDown(GLFW.GLFW_KEY_LEFT)) camera.rotation.y += 1.0f * time.getDeltaTime();

            renderer.render(mainScene, camera, projection);
            window.update();
        }

        shader.destroy();
        window.destroy();
        cubeMesh.destroy();
    }
}