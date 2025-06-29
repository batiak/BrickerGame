package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * The `Ball` class represents a ball object in the Bricker game.
 * It extends the `GameObject` class and includes information about collision handling and a collision sound.
 * @author Batia
 */
public class Ball extends GameObject {
    private static final int MAX_TURBO_COLLISIONS = 6;
    private static final float SPEED_MULTIPLIER = 1.4f;
    private final Renderable originalRenderer;
    private boolean isTurbo = false;
    private int collisionCounter = 0;
    private final Sound collisionSound;

    /**
     * Construct a new Ball instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound The sound to play upon collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.originalRenderer = renderable;
    }

    /**
     * This method is called whenever the ball collides with another object.
     * It reverses the ball's velocity based on the collision normal and plays the collision sound.
     *
     * @param otherObject The other game object involved in the collision.
     * @param collision   The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject otherObject, Collision collision) {
        super.onCollisionEnter(otherObject, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        if (isTurbo) {
            handleTurboCollision();
        }
    }

    /**
     * Deactivates the turbo mode for the ball.
     * Resets the ball's speed and appearance to their original state,
     * disables turbo mode, and resets the turbo collision counter.
     */
    private void deactivateTurbo() {
        // Reset ball's speed and renderable
        setVelocity(this.getVelocity().mult(1 / SPEED_MULTIPLIER));
        this.renderer().setRenderable(originalRenderer);
        this.isTurbo = false;
        collisionCounter = 0;
    }

    /**
     * Handles a collision while the ball is in turbo mode.
     * Increments the turbo collision counter and deactivates turbo mode
     * if the maximum number of turbo collisions has been reached.
     */
    private void handleTurboCollision() {
        if (collisionCounter <= MAX_TURBO_COLLISIONS) {
            collisionCounter++;
        }
        if (collisionCounter > MAX_TURBO_COLLISIONS) {
            deactivateTurbo();
        }
    }

    /**
     * Retrieves the current turbo status of the ball.
     * @return true if the ball is in turbo mode, false otherwise.
     */
    public boolean getIsTurbo() {
        return isTurbo;
    }

    /**
     * Sets the turbo status of the ball.
     * Updates the ball's turbo mode to the provided status and resets
     * the turbo collision counter to zero.
     * @param turboStatus The new turbo mode status (true for enabled, false for disabled).
     */
    public void setTurbo(boolean turboStatus) {
        isTurbo = turboStatus;
        collisionCounter = 0;
    }

    /**
     * Returns the number of collisions with bricks.
     * @return The collision counter.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}


