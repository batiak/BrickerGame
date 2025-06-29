package bricker.brick_strategies;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;

 /**
 * A collision strategy that handles the collision event between a brick and another game object.
 * When a collision is detected, the brick is removed, and a heart object is instantiated and falls down,
 * potentially granting the player an extra life if collected.
 * @author Batia
 */
public class ExtraLifeCollisionStrategy implements CollisionStrategy{
    private static final float FALLING_HEART_SPEED = 20;
    private final int HEART_SIZE = 20;
    private final Vector2 HEART_SIZE_DIMENSIONS = new Vector2(HEART_SIZE, HEART_SIZE);
    private final Renderable heartRenderable;
    private final Vector2 heartPositionOnBoard;
    private final BrickerGameManager brickerGameManager;
    private Heart fallingHeart;

     /**
      * Constructor for the ExtraLifeCollisionStrategy.
      * @param heartRenderable     The renderable for the heart object.
      * @param heartPositionOnBoard The initial position of the heart on the board.
      * @param brickerGameManager  The game manager that controls the game's logic.
      */
    public ExtraLifeCollisionStrategy(Renderable heartRenderable,
      Vector2 heartPositionOnBoard, BrickerGameManager brickerGameManager ) {
        this.heartRenderable = heartRenderable;
        this.heartPositionOnBoard = heartPositionOnBoard;
        this.brickerGameManager = brickerGameManager;
    }

     /**
      * Handles the collision between two game objects. Removes the brick from the game and spawns
      * a falling heart that can grant an extra life to the player if collected.
      * @param object1 The first game object involved in the collision, typically the brick.
      * @param object2 The second game object involved in the collision.
      */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickerGameManager.removeBrick(object1);
        fallingHeart = new Heart(heartPositionOnBoard, HEART_SIZE_DIMENSIONS, heartRenderable,
                                                                                        brickerGameManager);
        fallingHeart.setVelocity(Vector2.DOWN.mult(FALLING_HEART_SPEED));
        brickerGameManager.addObjectToGameObjects(fallingHeart);
        // add heart and update lives in manager


    }
}
