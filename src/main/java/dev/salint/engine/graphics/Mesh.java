package dev.salint.engine.graphics;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vao;
    private final int vbo;
    private final int ebo;
    private final int vertexCount;

    public Mesh(float[] vertices, int[] indices) {

        vertexCount = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(
                GL_ARRAY_BUFFER,
                vertices,
                GL_STATIC_DRAW
        );

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        glBufferData(
                GL_ELEMENT_ARRAY_BUFFER,
                indices,
                GL_STATIC_DRAW
        );

        glVertexAttribPointer(
                0,
                3,
                GL_FLOAT,
                false,
                8 * Float.BYTES,
                0
        );

        glEnableVertexAttribArray(0);

        glVertexAttribPointer(
                1,
                3,
                GL_FLOAT,
                false,
                8 * Float.BYTES,
                3 * Float.BYTES
        );

        glEnableVertexAttribArray(1);

        glVertexAttribPointer(
                2,
                2,
                GL_FLOAT,
                false,
                8 * Float.BYTES,
                6 * Float.BYTES
        );

        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    public void draw() {
        glBindVertexArray(vao);

        glDrawElements(
                GL_TRIANGLES,
                vertexCount,
                GL_UNSIGNED_INT,
                0
        );

        glBindVertexArray(0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}