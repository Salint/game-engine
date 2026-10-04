package dev.salint.engine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

public class Window {

    private final long handle;
    private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final GLFWKeyCallback keyCallback;

    public Window(int width, int height, String title) {

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        handle = GLFW.glfwCreateWindow(
                width,
                height,
                title,
                0,
                0
        );

        if (handle == 0) {
            GLFW.glfwTerminate();
            throw new IllegalStateException("Unable to create GLFW window");
        }

        keyCallback = GLFW.glfwSetKeyCallback(
                handle,
                (window, key, scancode, action, mods) -> {

                    if (key >= 0 && key < keys.length) {
                        if (action == GLFW.GLFW_PRESS) {
                            keys[key] = true;
                        }
                        else if (action == GLFW.GLFW_RELEASE) {
                            keys[key] = false;
                        }
                    }
                }
        );

        GLFW.glfwMakeContextCurrent(handle);

        GLFW.glfwSwapInterval(1);
        GL.createCapabilities();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        keyCallback.close();

        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }
}