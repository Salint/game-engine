package dev.salint.engine.graphics;

public class Material {

    public final Shader shader;
    public final Texture texture;

    public Material(Shader shader, Texture texture) {
        this.shader = shader;
        this.texture = texture;
    }
}