package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * This class represents a paddle in the game.
 * @author Batia
 */
public class Paddle extends GameObject {
    private static final int MOVEMENT_SPEED = 300;
    private static final float PADDLE_SIZE = 100;
    private final Vector2 topLeftCorner;
    private final UserInputListener inputListener;
    private final Vector2 windowDimension;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, Vector2 windowDimension) {
        super(topLeftCorner, dimensions, renderable);
        this.topLeftCorner = topLeftCorner;
        this.inputListener = inputListener;
        this.windowDimension = windowDimension;

    }

    /**
     * Updates the state of this GameObject. Called once per frame.
     * This method handles the movement of the paddle based on user input
     * and ensures the paddle stays within the boundaries of the game window.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDir = movementDir.add(Vector2.LEFT.mult(MOVEMENT_SPEED));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movementDir = movementDir.add(Vector2.RIGHT.mult(MOVEMENT_SPEED));
        }
        setVelocity(movementDir);

        if (this.getTopLeftCorner().x() <= 0){
            this.setTopLeftCorner(new Vector2(0, topLeftCorner.y()));
        }
        if (this.getTopLeftCorner().x() >= windowDimension.x() - PADDLE_SIZE){
            this.setTopLeftCorner(
                    new Vector2(windowDimension.x() - PADDLE_SIZE, topLeftCorner.y()));
        }
    }

}


