package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;

/**
 * TurboStrategy is a collision strategy that applies a turbo effect to the ball
 * when it collides with a brick. The turbo effect increases the ball's speed
 * and changes its appearance for a limited duration.
 * @author Batia
 */
public class TurboStrategy implements CollisionStrategy {
    private static final float SPEED_MULTIPLIER = 1.4f;
    private final Renderable turboRenderable;
    BrickerGameManager manager;

    /**
     * Constructs a TurboStrategy instance.
     * @param turboRenderable Renderable to be used for the ball during turbo mode.
     * @param manager Reference to the BrickerGameManager for managing game state.
     */
    public TurboStrategy(Renderable turboRenderable, BrickerGameManager manager) {
        this.turboRenderable = turboRenderable;
        this.manager = manager;
    }

    /**
     * Handles collision between two game objects. If the collision involves a ball
     * that is not in turbo mode, the turbo effect is activated on the ball.
     * @param object1 The first game object involved in the collision (e.g., brick).
     * @param object2 The second game object involved in the collision (e.g., ball).
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        manager.removeBrick(object1);
        Ball gameBall = (Ball) object2;
        boolean isPuck = gameBall.getTag().equals("Puck");
        if ((!isPuck) && (!(gameBall).getIsTurbo())) {
            activateTurbo(gameBall);
        }
    }

    /**
     * Activates the turbo effect on the given ball. The turbo effect increases the ball's speed
     * and changes its appearance to the turbo renderable.
     * @param theBall The ball to activate the turbo effect on.
     */
    private void activateTurbo(Ball theBall) {
        // Save the original renderer
        // set new renderer and increase speed
        theBall.setVelocity(theBall.getVelocity().mult(SPEED_MULTIPLIER));
        theBall.renderer().setRenderable(turboRenderable);
        theBall.setTurbo(true);

    }
}

