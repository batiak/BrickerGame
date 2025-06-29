package bricker.brick_strategies;

import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * Factory class for creating collision strategies for bricks in the game.
 * @author Batia
 */
public class CollisionStrategyFactory {
    // Paths for assets
    private static final String HEART_IMG_PATH = "assets/heart.png";
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";
    private static final String TURBO_IMG_PATH = "assets/redball.png";
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop.wav";

    // Constants for strategy probabilities
    private static final float HALF = 0.5f;
    private static final int BASIC_STRATEGY_START = 0;
    private static final int BASIC_STRATEGY_END = 4;
    private static final int PUCK_STRATEGY_INDEX = 5;
    private static final int EXTRA_PADDLE_STRATEGY_INDEX = 6;
    private static final int TURBO_STRATEGY_INDEX = 7;
    private static final int EXTRA_LIFE_STRATEGY_INDEX = 8;
    private static final int DOUBLE_STRATEGY_INDEX = 9;
    private static final int MAX_RANDOM_VALUE = 10;
    private static final int MAX_DOUBLE_STRATEGIES = 3;

    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final int brickLen;

    /**
     * Constructs a new CollisionStrategyFactory instance.
     *
     * @param brickerGameManager The game manager instance.
     * @param imageReader        Reader for images.
     * @param soundReader        Reader for sounds.
     * @param inputListener      Listener for user input.
     * @param brickLen           The length of the brick.
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager, ImageReader imageReader,
                                    SoundReader soundReader, UserInputListener inputListener,
                                    int brickLen) {
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.brickLen = brickLen;
    }

    /**
     * Creates Renderable image to use in game from given path.
     * @param path Path of the file.
     * @param topLeftTransparency Whether the top-left corner is transparent.
     * @return Renderable object created from the image.
     */
    private Renderable createImgFromPath(String path, boolean topLeftTransparency) {
        return imageReader.readImage(path, topLeftTransparency);
    }

    /**
     * Creates an object of ExtraLifeCollisionStrategy for a brick.
     * @param brickPosition Position of the brick on the board.
     * @param brickLen Length of the brick.
     * @return ExtraLifeCollisionStrategy object.
     */
    private ExtraLifeCollisionStrategy createExtraLifeStrategy(Vector2 brickPosition, int brickLen) {
        Renderable heartImg = createImgFromPath(HEART_IMG_PATH, true);
        Vector2 heartPosition = new Vector2((brickPosition.x() + HALF * brickLen), brickPosition.y());
        return new ExtraLifeCollisionStrategy(heartImg, heartPosition, brickerGameManager);
    }

    /**
     * Creates a PuckStrategy object.
     * @param brickPosition Position of the brick.
     * @param brickerGameManager The game manager instance.
     * @param brickLen Length of the brick.
     * @return PuckStrategy object.
     */
    private PuckStrategy createPuckStrategy(Vector2 brickPosition,
                                            BrickerGameManager brickerGameManager, int brickLen) {
        Renderable puckImage = createImgFromPath(PUCK_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        Vector2 puckPosition = new Vector2((brickPosition.x() + HALF * brickLen), brickPosition.y());
        return new PuckStrategy(puckPosition, puckImage, collisionSound, brickerGameManager);
    }

    /**
     * Generates a random collision strategy for a brick.
     *
     * @param maxRandom The upper limit for random number generation.
     * @return A CollisionStrategy object.
     */
    public CollisionStrategy generateBrickStrategy(int maxRandom, Vector2 curBrickPositionOnBoard) {
        Random random = new Random();
        int randomNumber = random.nextInt(maxRandom);

        return switch (randomNumber) {
            case BASIC_STRATEGY_START, 1, 2, 3, BASIC_STRATEGY_END -> createBasicStrategy();
            case PUCK_STRATEGY_INDEX -> createPuckStrategy(curBrickPositionOnBoard, brickerGameManager, brickLen);
            case EXTRA_PADDLE_STRATEGY_INDEX -> createExtraPaddleStrategy();
            case TURBO_STRATEGY_INDEX -> createTurboStrategy();
            case EXTRA_LIFE_STRATEGY_INDEX -> createExtraLifeStrategy(curBrickPositionOnBoard, brickLen);
            case DOUBLE_STRATEGY_INDEX -> createDoubleStrategy(curBrickPositionOnBoard);
            default -> null;
        };
    }

    /**
     * Creates a DoubleStrategy object.
     * @return DoubleStrategy object.
     */
    private CollisionStrategy createDoubleStrategy(Vector2 curBrickPositionOnBoard) {
        CollisionStrategy[] chosenStrategies = new CollisionStrategy[MAX_DOUBLE_STRATEGIES];
        chosenStrategies[0] = generateBrickStrategy(MAX_RANDOM_VALUE, curBrickPositionOnBoard);
        if (chosenStrategies[0] instanceof DoubleStrategy) {
            chosenStrategies[1] = generateBrickStrategy(MAX_RANDOM_VALUE - 1, curBrickPositionOnBoard);
        } else {
            chosenStrategies[1] = generateBrickStrategy(MAX_RANDOM_VALUE, curBrickPositionOnBoard);
        }
        if (handleAnotherDouble(chosenStrategies, curBrickPositionOnBoard)) {
            return new DoubleStrategy(chosenStrategies, MAX_DOUBLE_STRATEGIES);
        }
        return new DoubleStrategy(chosenStrategies, MAX_DOUBLE_STRATEGIES - 1);
    }

    /**
     * Handles cases where another DoubleStrategy is chosen in a DoubleStrategy.
     *
     * @param chosen An array of chosen strategies.
     * @param curBrickPositionOnBoard Position of the brick.
     * @return True if another DoubleStrategy was chosen, false otherwise.
     */
    private boolean handleAnotherDouble(CollisionStrategy[] chosen, Vector2 curBrickPositionOnBoard) {
        if (chosen[0] instanceof DoubleStrategy) {
            chooseDoubleAgain(chosen, 0, curBrickPositionOnBoard);
            return true;
        } else if (chosen[1] instanceof DoubleStrategy) {
            chooseDoubleAgain(chosen, 1, curBrickPositionOnBoard);
            return true;
        }
        return false;
    }

    /**
     * Chooses another 2 strategies if a DoubleStrategy is chosen the 2nd time.
     * @param chosen An array of chosen strategies.
     * @param indexOfDouble The index of the DoubleStrategy in the array.
     */
    private void chooseDoubleAgain(CollisionStrategy[] chosen, int indexOfDouble, Vector2 brickPosition) {
        chosen[indexOfDouble] = generateBrickStrategy(MAX_RANDOM_VALUE - 1, brickPosition);
        chosen[2] = generateBrickStrategy(MAX_RANDOM_VALUE - 1, brickPosition);
    }

    /**
     * Creates a TurboStrategy object.
     * @return TurboStrategy object.
     */
    private CollisionStrategy createTurboStrategy() {
        Renderable turboImg = createImgFromPath(TURBO_IMG_PATH, true);
        return new TurboStrategy(turboImg, brickerGameManager);
    }

    /**
     * Creates an ExtraPaddleStrategy object.
     * @return ExtraPaddleStrategy object.
     */
    private CollisionStrategy createExtraPaddleStrategy() {
        Renderable paddleImg = createImgFromPath(PADDLE_IMG_PATH, true);
        return new ExtralPaddleStrategy(brickerGameManager, inputListener, paddleImg);
    }

    /**
     * Creates a BasicCollisionStrategy object.
     * @return BasicCollisionStrategy object.
     */
    private CollisionStrategy createBasicStrategy() {
        return new BasicCollisionStrategy(brickerGameManager);
    }
}
