package dev.salint.engine.graphics;

import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.GameObject;
import dev.salint.engine.scene.Light;
import dev.salint.engine.scene.Scene;
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
            Scene scene,
            Camera camera,
            Matrix4f projection
    ) {
       for(GameObject gameObject : scene.getGameObjects()) {

           Light light = scene.getLights().getFirst();

           gameObject.meshRenderer.material.shader.bind();

           gameObject.meshRenderer.material.shader.setMatrix4f("model", gameObject.transform.getModelMatrix());
           gameObject.meshRenderer.material.shader.setMatrix4f("view", camera.getViewMatrix());
           gameObject.meshRenderer.material.shader.setMatrix4f("projection", projection);
           gameObject.meshRenderer.material.shader.setInt("textureSampler", 0);
           gameObject.meshRenderer.material.shader.setVector3f(
                   "lightDirection",
                   light.direction
           );

           gameObject.meshRenderer.material.shader.setVector3f(
                   "lightColor",
                   light.color
           );

           gameObject.meshRenderer.material.texture.bind();

           gameObject.meshRenderer.mesh.draw();
        }
    }
}