package dev.salint.engine.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<GameObject> gameObjects = new ArrayList<>();

    public void add(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

}
