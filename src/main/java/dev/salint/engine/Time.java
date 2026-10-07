package dev.salint.engine;

import org.lwjgl.glfw.GLFW;

public class Time {

    private double lastTime;
    private float deltaTime;

    public Time() {
        lastTime = GLFW.glfwGetTime();
    }

    public void update() {
        double currentTime = GLFW.glfwGetTime();

        deltaTime = (float) (currentTime - lastTime);

        lastTime = currentTime;
    }

    public float getDeltaTime() {
        return deltaTime;
    }
}