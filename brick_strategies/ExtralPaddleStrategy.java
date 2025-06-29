package bricker.brick_strategies;


import danogl.gui.rendering.Renderable;
import bricker.gameobjects.AdditionalPaddle;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * This class creates represents a collision strategy when an extra paddle is creates
 * when a ball collides with the brick
 * @author Batia
 */
public class ExtralPaddleStrategy implements CollisionStrategy {
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_WIDTH = 100;
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
    private final BrickerGameManager brickerGameManager;
    private final UserInputListener inputListener;
    private final Renderable paddleImage;
    private final Vector2 windowDimensions;

    /**
     * Constructor for the extra paddle strategy.
     * @param inputListener an input listener.
     * @param paddleImage The image of the paddle.
     */
    public ExtralPaddleStrategy(BrickerGameManager brickerGameManager, UserInputListener inputListener,
                                Renderable paddleImage) {
        this.brickerGameManager = brickerGameManager;
        this.inputListener = inputListener;
        this.paddleImage = paddleImage;
        this.windowDimensions = brickerGameManager.getWindowDimensions();
    }

    /**
     * The method is called when a ball collides with a brick. checks if there is an extra paddle in the game,
     * if not creates one.

     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        brickerGameManager.removeBrick(obj1);
        if (brickerGameManager.getExtraPaddle() == null){
            createAdditionalPaddle();
        }
    }

    /**
     * Creates the extra paddle.
     */
    private void createAdditionalPaddle() {
        AdditionalPaddle anotherPaddle =
            new AdditionalPaddle(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2),
            PADDLE_DIMENSIONS, paddleImage, inputListener, windowDimensions, brickerGameManager);
        brickerGameManager.setExtraPaddle(anotherPaddle);
        brickerGameManager.addObjectToGameObjects(anotherPaddle);
    }
}
