package dev.salint.engine.graphics;

import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.GameObject;
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
            GameObject gameObject,
            Camera camera,
            Matrix4f projection
    ) {
       gameObject.meshRenderer.material.shader.bind();

        gameObject.meshRenderer.material.shader.setMatrix4f("model", gameObject.transform.getModelMatrix());
        gameObject.meshRenderer.material.shader.setMatrix4f("view", camera.getViewMatrix());
        gameObject.meshRenderer.material.shader.setMatrix4f("projection", projection);
        gameObject.meshRenderer.material.shader.setInt("textureSampler", 0);

        gameObject.meshRenderer.material.texture.bind();

        gameObject.meshRenderer.mesh.draw();
    }
}