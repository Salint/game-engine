package dev.salint.engine.graphics;

public class MeshRenderer {
    public Mesh mesh;
    public Material material;

    public MeshRenderer(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }
}
