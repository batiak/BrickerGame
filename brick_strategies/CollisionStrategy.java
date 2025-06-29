package bricker.brick_strategies;

import danogl.GameObject;

/**
 * CollisionStrategy is an interface for defining how collisions between
 * game objects should be handled.
 * @author Batia
 */
public interface CollisionStrategy {
    /**
     * Handles the collision between two game objects.
     *
     * @param object1 The first game object involved in the collision.
     * @param object2 The second game object involved in the collision.
     */
    void onCollision(GameObject object1, GameObject object2);
}
