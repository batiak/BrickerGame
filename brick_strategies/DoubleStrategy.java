package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A strategy that applies multiple collision strategies when a collision occurs.
 * @author Batia
 */
public class DoubleStrategy implements CollisionStrategy{

    private final CollisionStrategy[] collisionStrategies;
    private final int numOfStrategies;

    /**
     * Constructs a DoubleStrategy instance.
     * @param collisionStrategies An array of collision strategies to apply.
     * @param numOfStrategies The number of strategies to apply.
     */
    public DoubleStrategy(CollisionStrategy[] collisionStrategies, int numOfStrategies) {
        this.collisionStrategies = collisionStrategies ;
        this.numOfStrategies = numOfStrategies;
    }

    /**
     * Applies all collision strategies in sequence when a collision occurs.
     * @param object1 The first object involved in the collision.
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        for (int i = 0; i < numOfStrategies; i++) {
            collisionStrategies[i].onCollision(object1, object2);
        }
    }
}
