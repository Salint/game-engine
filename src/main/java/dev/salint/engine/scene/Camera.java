package dev.salint.engine.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f rotation = new Vector3f(0.0f, 0.0f, 0.0f);

    public Matrix4f getViewMatrix() {

        return new Matrix4f()
                .rotateXYZ(
                        -rotation.x,
                        -rotation.y,
                        -rotation.z
                )
                .translate(
                        -position.x,
                        -position.y,
                        -position.z
                );
    }
}