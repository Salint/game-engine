package dev.salint.engine.graphics;

import dev.salint.engine.scene.Camera;
import dev.salint.engine.scene.GameObject;
import dev.salint.engine.scene.Light;
import dev.salint.engine.scene.Scene;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    public Renderer() {
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.8f, 1f, 1.0f);
        glCullFace(GL_BACK);
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

           gameObject.meshRenderer.material.shader.bind();

           gameObject.meshRenderer.material.shader.setMatrix4f("model", gameObject.transform.getModelMatrix());
           gameObject.meshRenderer.material.shader.setMatrix4f("view", camera.getViewMatrix());
           gameObject.meshRenderer.material.shader.setMatrix4f("projection", projection);
           gameObject.meshRenderer.material.shader.setInt("textureSampler", 0);

           int lightCount = Math.min(
                   scene.getLights().size(),
                   4
           );

           gameObject.meshRenderer.material.shader.setInt(
                   "lightCount",
                   lightCount
           );

           for (int i = 0; i < lightCount; i++) {

               Light light = scene.getLights().get(i);

               gameObject.meshRenderer.material.shader.setVector3f(
                       "lights[" + i + "].direction",
                       light.direction
               );

               gameObject.meshRenderer.material.shader.setVector3f(
                       "lights[" + i + "].color",
                       light.color
               );
           }

           gameObject.meshRenderer.material.texture.bind();

           gameObject.meshRenderer.mesh.draw();
        }
    }
}