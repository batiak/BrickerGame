package bricker.brick_strategies;

import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import java.util.Random;

/**
 * A collision strategy that handles the collision event between a brick and another game object.
 * When a collision is detected, the brick is removed, and two puck objects are instantiated and
 * move in random directions.
 * @author Batia
 */
public class PuckStrategy implements CollisionStrategy{
    private static final float BALL_SPEED = 100;
    private final int PUCK_SIZE = 15;
    private final Vector2 PUCK_SIZE_DIMENSIONS = new Vector2(PUCK_SIZE, PUCK_SIZE);
    private final Renderable renderable;
    private final Sound collisionSound;
    private final Vector2 puckPositionOnBoard;
    private final BrickerGameManager brickerGameManager;
    private Ball puck1;
    private Ball puck2;

    /**
     * Constructor for the PuckStrategy.
     * @param puckPositionOnBoard The initial position of the pucks on the board.
     * @param puckImg             The renderable image for the pucks.
     * @param collisionSound      The sound to play on collision.
     * @param brickerGameManager  The game manager that controls the game's logic.
     */
    public PuckStrategy(Vector2 puckPositionOnBoard, Renderable puckImg, Sound collisionSound,
                        BrickerGameManager brickerGameManager) {
        this.renderable = puckImg;
        this.collisionSound = collisionSound;
        this.puckPositionOnBoard = puckPositionOnBoard;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects. Removes the brick from the game and spawns
     * two pucks that move in random directions.
     * @param object1 The first game object involved in the collision, typically the brick.
     * @param object2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickerGameManager.removeBrick(object1);
        this.puck1 = new Ball(puckPositionOnBoard, PUCK_SIZE_DIMENSIONS, renderable, collisionSound);
        this.puck2 = new Ball(puckPositionOnBoard, PUCK_SIZE_DIMENSIONS, renderable, collisionSound);
        puck1.setTag("Puck");
        puck2.setTag("Puck");
        puck1.setVelocity(getRandomPuckDirection());
        puck2.setVelocity(getRandomPuckDirection());
        brickerGameManager.addObjectToGameObjects(puck1);
        brickerGameManager.addObjectToGameObjects(puck2);
    }

    /**
     * Generates a random direction for the puck to move in.
     * @return A Vector2 representing the direction and speed of the puck.
     */
    private Vector2 getRandomPuckDirection() {
        Random rand = new Random();
        double angle  = rand.nextDouble() *Math.PI;
        float velocityX = (float)Math.cos(angle) * BALL_SPEED;
        float velocityY = (float)Math.sin(angle) * BALL_SPEED;
        return new Vector2(velocityX,velocityY);
    }


}

