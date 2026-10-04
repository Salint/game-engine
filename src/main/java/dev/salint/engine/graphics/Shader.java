package dev.salint.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int program;
    private final Map<String, Integer> uniformLocations = new HashMap<>();

    public Shader(String vertexPath, String fragmentPath) {

        String vertexSource = readResource(vertexPath);
        String fragmentSource = readResource(fragmentPath);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException(
                    "Vertex shader compilation failed:\n" +
                            glGetShaderInfoLog(vertexShader)
            );
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException(
                    "Fragment shader compilation failed:\n" +
                            glGetShaderInfoLog(fragmentShader)
            );
        }

        program = glCreateProgram();

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException(
                    "Shader program linking failed:\n" +
                            glGetProgramInfoLog(program)
            );
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void setMatrix4f(String name, Matrix4f matrix) {

        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalArgumentException(
                    "Uniform not found: " + name
            );
        }

        float[] values = new float[16];

        matrix.get(values);

        glUniformMatrix4fv(
            location,
            false,
            values
        );
    }

    public void setInt(String name, int value) {
        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalArgumentException(
                    "Uniform not found: " + name
            );
        }

        glUniform1i(location, value);
    }

    public void setVector3f(String name, Vector3f value) {
        int location = getUniformLocation(name);

        if (location == -1) {
            throw new IllegalArgumentException(
                    "Uniform not found: " + name
            );
        }

        glUniform3f(
                location,
                value.x,
                value.y,
                value.z
        );
    }

    public void destroy() {
        glDeleteProgram(program);
    }

    private static String readResource(String path) {
        try (var input = Shader.class.getResourceAsStream(path)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Shader resource not found: " + path
                );
            }

            return new String(
                    input.readAllBytes(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

        } catch (java.io.IOException e) {
            throw new RuntimeException(
                    "Failed to read shader: " + path,
                    e
            );
        }
    }
    private int getUniformLocation(String name) {

        Integer cached = uniformLocations.get(name);

        if (cached != null) {
            return cached;
        }

        int location = glGetUniformLocation(program, name);

        if (location == -1) {
            throw new IllegalArgumentException(
                    "Uniform not found: " + name
            );
        }

        uniformLocations.put(name, location);

        return location;
    }
}
