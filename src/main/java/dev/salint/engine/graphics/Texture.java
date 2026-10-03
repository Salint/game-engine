package dev.salint.engine.graphics;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private final int id;

    public Texture(String path) {

        ByteBuffer imageData;

        try (InputStream input =
                     Texture.class.getResourceAsStream(path)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Texture not found: " + path
                );
            }

            byte[] bytes = input.readAllBytes();

            imageData = ByteBuffer.allocateDirect(bytes.length);
            imageData.put(bytes);
            imageData.flip();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read texture: " + path,
                    e
            );
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer pixels = STBImage.stbi_load_from_memory(
                    imageData,
                    width,
                    height,
                    channels,
                    4
            );

            if (pixels == null) {
                throw new IllegalStateException(
                        "Failed to decode texture: " +
                                STBImage.stbi_failure_reason()
                );
            }

            id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_NEAREST
            );

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width.get(0),
                    height.get(0),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels
            );

            STBImage.stbi_image_free(pixels);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void destroy() {
        glDeleteTextures(id);
    }
}