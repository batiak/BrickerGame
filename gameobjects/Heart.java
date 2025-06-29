package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * This class represents a heart in the game.
 * @author Batia
 */
public class Heart extends GameObject {
    private final int FALLING_HEART_SPEED = 100;
    private final String HEART_IMG_PATH = "assets/heart.png";
    private final BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * defines the heart can collide only with the paddle
     * @param other The other GameObject.
     * @return true if collision is legal and false if not
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        //return super.shouldCollideWith(other);
        return other == brickerGameManager.getMainPaddle() || other == brickerGameManager.getExtraPaddle();
    }

    /**
     * Called when a collision occurs with this GameObject.
     * This method will add a heart to the lives collection, update the life numeric counter,
     * and remove this object from the game.
     * @param other The GameObject this object collided with.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.addHeartToLivesCollection();
        brickerGameManager.updateLifeNumericCounter();
        brickerGameManager.removeObjectFromGameObject(this);
    }

    /**
     * Updates the state of this GameObject. Called once per frame.
     * This method checks if the object has moved beyond the bottom boundary of the window,
     * and if so, removes the object from the game.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > brickerGameManager.getWindowDimensions().y()){
            brickerGameManager.removeObjectFromGameObject(this);
        }
    }
}
