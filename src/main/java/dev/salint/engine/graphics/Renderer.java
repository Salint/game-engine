package dev.salint.engine.graphics;

import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.Transform;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Renderer {

    public void begin() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(
            Mesh mesh,
            Shader shader,
            Texture texture,
            Transform transform,
            Camera camera,
            Matrix4f projection
    ) {
        shader.bind();

        shader.setMatrix4f("model", transform.getModelMatrix());
        shader.setMatrix4f("view", camera.getViewMatrix());
        shader.setMatrix4f("projection", projection);
        shader.setInt("textureSampler", 0);

        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        mesh.draw();
    }
}