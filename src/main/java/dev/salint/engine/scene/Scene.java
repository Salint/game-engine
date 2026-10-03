package dev.salint.engine.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<Light> lights = new ArrayList<>();

    public void add(GameObject gameObject) {
        gameObjects.add(gameObject);
    }
    public void add(Light light) {
        lights.add(light);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
    public List<Light> getLights() {
        return lights;
    }

}
