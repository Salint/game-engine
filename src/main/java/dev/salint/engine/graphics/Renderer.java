package dev.salint.engine.graphics;

import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.Transform;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    public Renderer() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    public void begin() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(
            Mesh mesh,
            Material material,
            Transform transform,
            Camera camera,
            Matrix4f projection
    ) {
        material.shader.bind();

        material.shader.setMatrix4f("model", transform.getModelMatrix());
        material.shader.setMatrix4f("view", camera.getViewMatrix());
        material.shader.setMatrix4f("projection", projection);
        material.shader.setInt("textureSampler", 0);

        material.texture.bind();

        mesh.draw();
    }
}