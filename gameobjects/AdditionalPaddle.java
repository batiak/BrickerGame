package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * The `AdditionalPaddle` class represents an additional paddle object in the Bricker game,
 * extending the functionality of the `Paddle` class. It includes collision handling and a
 * collision counter to track the number of collisions with other game objects.
 */
public class AdditionalPaddle extends Paddle {
    private static final int MOVEMENT_SPEED = 300;
    private final int MAX_COLLISION = 4;
    private final UserInputListener inputListener;
    private final Vector2 windowDimension;
    private final BrickerGameManager brickerGameManager;
    // Fields
    private int collisionCounter = 0; // Counter for the number of collisions

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner   Position of the object, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderable      The renderable representing the object. Can be null, in which case
     *                        the GameObject will not be rendered.
     * @param inputListener   Keyboard input from user
     * @param windowDimension The dimensions of the game window.
     */
    public AdditionalPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                            UserInputListener inputListener, Vector2 windowDimension,
                            BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimension);
        this.inputListener = inputListener;
        this.windowDimension = windowDimension;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Called when a collision occurs with another GameObject.
     * @param other The other GameObject involved in the collision.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) {
            collisionCounter++;
            if (collisionCounter == MAX_COLLISION) {
                resetAdditionalPaddle();
            }
        }
    }

    /**
     * rests the AdditionalPaddle and removes it from game after the relevant num of collisions
     */
    private void resetAdditionalPaddle() {
        brickerGameManager.setExtraPaddle(null);
        brickerGameManager.removeObjectFromGameObject(this);
        collisionCounter = 0;
    }

    /**
     * Gets the count of collisions that the additional paddle has experienced.
     * @return The collision counter.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}

