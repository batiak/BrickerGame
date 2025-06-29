package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * BasicCollisionStrategy defines the behavior for a basic collision strategy
 * where a brick is removed from the game when a collision is detected.
 * @author Batia
 * */
public class BasicCollisionStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for BasicCollisionStrategy.
     * @param brickerGameManager The game manager that manages the game objects and game state.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Defines the actions to take when a collision is detected between two game objects.
     * In this basic strategy, the brick (object1) is removed from the game.
     * @param object1 The first game object involved in the collision (typically the brick).
     * @param object2 The second game object involved in the collision (e.g., the ball).
     */
     @Override
    public void onCollision(GameObject object1, GameObject object2) {
         brickerGameManager.removeBrick(object1);

        }
    }

