package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;

import java.awt.*;
import java.util.Random;

/**
 * BrickerGameManager is the bricker.main game manager class for the Bricker game.
 * It handles the initialization and management of game objects, user input, and game state.
 * @author Batia
 */
public class BrickerGameManager extends GameManager {
    // BruckGameManager constants
    private final static String BALL_IMG_PATH = "assets/ball.png";
    private final static String HEART_IMG_PATH = "assets/heart.png";
    private final static String PADDLE_IMG_PATH = "assets/paddle.png";
    private final static String BRICK_IMG_PATH = "assets/brick.png";
    private final static String BACKGROUND_IMG_PATH = "assets/DARK_BG2_small.jpeg";
    private final static int USER_ARGS = 2;
    private final static int BRICK_LINES_NUM_ARG = 0;
    private final static int BRICKS_PER_LINE_ARG = 1;
    private final static int DEFAULT_LIVES_NUM = 3;
    private final static int DEFAULT_BRICK_LINES_NUM = 7;
    private final static int DEFAULT_BRICKS_PER_LINE = 8;
    private final static String LOSE_MESSAGE = "You lose! Play again?";
    private final static String WIN_MESSAGE =  "You win! Play again?";
    private static final String COLLISION_SOUND_PATH = "assets/blop.wav";
    private final int WALL_WIDTH = 10;
    private final static int BRICK_WIDTH = 15;
    private final static int HEART_SIZE = 20;
    private final int BALL_SPEED = 100;
    private final static int RANDOM_MAX = 10;
    private final static int PADDLE_HEIGHT = 30;
    private final static int WINDOW_HEIGHT = 500;
    private final static int WINDOW_WIDTH = 700;
    private final static int NUMERIC_LIFE_DIMENSIONS = 20;
    private final static int BALL_DIMENSIONS = 20;
    private final static int PADDLE_DIMENSIONS = 100;
    // BrickGameManager fields
    private Paddle paddle;
    private AdditionalPaddle extraPaddle;
    private Ball ball;
    private final Heart[] hearts = new Heart[4];
    private final int brickLines;
    private final int bricksPerLine;
    private static Vector2 windowDimensions;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputLister;
    private static WindowController windowController;
    private static int livesLeft;
    private int bricksLeft;
    private TextRenderable lifeNumericCounter;

    /**
     * Constructor for the BrickerGameManager class.
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions (width and height) of the game window.
     * @param brickLines       The number of rows of bricks in the game.
     * @param bricksPerLine    The number of bricks in each row.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int brickLines, int bricksPerLine)
    {
        super(windowTitle, windowDimensions);
        this.brickLines = brickLines;
        this.bricksPerLine = bricksPerLine;
        this.bricksLeft = brickLines * bricksPerLine;
    }

    /**
     * Bricker.bricker.main paddle getter
     * @return Paddle
     */
    public Paddle getMainPaddle() {
        return paddle;
    }

    /**
     * window dimensions getter
     * @return Vector2 representing window dimensions
     */
    public static Vector2 getWindowDimensions() {
        return windowDimensions;
    }

    /**
     * Getter for the window controller.
     * @return The window controller.
     */
    public static WindowController getWindowController() {
        return windowController;
    }

    /**
     * Getter for the extra paddle.
     * @return The extra paddle.
     */
    public AdditionalPaddle getExtraPaddle() {
        return extraPaddle;
    }

    /**
     * Setter for the extra paddle.
     * @param extraPaddle The extra paddle to set.
     */
    public void setExtraPaddle(AdditionalPaddle extraPaddle) {
        this.extraPaddle = extraPaddle;
    }

    /**
     * initializes a Bricker game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputLister = inputListener;
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        //add background
        createGameBackground();
        // create borders
        createGameBorders();
        // create paddle
        this.paddle = createPaddle(inputListener,
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_HEIGHT));
        // create bricks
        createBricks();
        // create ball
        createBall(soundReader);
        // init lives
        initializeLives();
    }

    /**
     * initializes lives for beginning of the game
     */
    private void initializeLives(){
        for (int i = 0; i < DEFAULT_LIVES_NUM; i++) {
            addHeartToLivesCollection();
        }
        this.lifeNumericCounter = new TextRenderable(Integer.toString(livesLeft));
        lifeNumericCounter.setColor(Color.green);
        GameObject lifeNumericCounterObject = new GameObject(new Vector2(5,
                (windowDimensions.y() - (NUMERIC_LIFE_DIMENSIONS + 5))),
                new Vector2(NUMERIC_LIFE_DIMENSIONS,NUMERIC_LIFE_DIMENSIONS), lifeNumericCounter);
        this.gameObjects().addGameObject(lifeNumericCounterObject);
    }

    /**
     * initializes game background
     */
    private void createGameBackground() {
        Renderable backGroundImage =
                imageReader.readImage(BACKGROUND_IMG_PATH, false);
        GameObject backGround = new GameObject(Vector2.ZERO, windowDimensions, backGroundImage);
        this.gameObjects().addGameObject(backGround, Layer.BACKGROUND);
    }

    /**
     * initialize ball for the game
     * @param soundReader: see initializeGame documentation
     */
    private void createBall(SoundReader soundReader) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIMENSIONS, BALL_DIMENSIONS),
                                                                        ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(windowDimensions.mult(0.5f));
        this.gameObjects().addGameObject(ball);
        // set ball angel
        float ballVelX = BALL_SPEED;
        float ballVelY= BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * creates Paddle
     * @param inputListener see initializeGame documentation
     */
    public Paddle createPaddle(UserInputListener inputListener, Vector2 location) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG_PATH, true);
        int PADDLE_LEN = 15;
        Paddle newPaddle = new Paddle(location, new Vector2(PADDLE_DIMENSIONS, PADDLE_LEN),
                paddleImage, inputListener, windowDimensions);
//        newPaddle.setCenter(location);
        this.gameObjects().addGameObject(newPaddle);
        return newPaddle;
    }


    /**
     * creates game borders
     */
    private void createGameBorders() {
        createWall(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()));
        createWall(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH));
        createWall(new Vector2 (windowDimensions.x(),0), new Vector2(WALL_WIDTH, windowDimensions.y()));
    }

    /**
     * create a wall for border of the game
     * @param position position for wall
     * @param dimensions dimension of wall
     */
    private void createWall(Vector2 position, Vector2 dimensions){

        GameObject leftWall = new GameObject(position,dimensions,null);
        this.gameObjects().addGameObject(leftWall);
    }

    /**
     * create Bricks for game
     */
    public void createBricks(){
        Renderable brickImage = imageReader.readImage(BRICK_IMG_PATH, false);
        int brickLen = (int)
                (windowDimensions.x() - (2 * WALL_WIDTH) - (bricksPerLine + 1)) / this.bricksPerLine;
        int startPositionY = WALL_WIDTH + 1;
        CollisionStrategyFactory strategyFactory = new CollisionStrategyFactory(this,
                imageReader, soundReader, inputLister, brickLen);
        for (int i = 0; i < brickLines; i++) {
            int startPositionX = WALL_WIDTH + 1;
            for (int j = 0; j < bricksPerLine; j++) {
                Vector2 brickPosition = new Vector2(startPositionX, startPositionY);
                CollisionStrategy strategy = strategyFactory.generateBrickStrategy(RANDOM_MAX, brickPosition);
                Brick curBrick =
                        new Brick(brickPosition, new Vector2(brickLen, BRICK_WIDTH), brickImage, strategy);
                this.gameObjects().addGameObject(curBrick);
                startPositionX += (brickLen + 1);
            }
            startPositionY += (BRICK_WIDTH + 1);
        }
    }

    /**
     * remove brick from game
     * @param brickToRemove Brick object to remove
     */
    public void removeBrick(GameObject brickToRemove){
        if (this.gameObjects().removeGameObject(brickToRemove)){
            bricksLeft --;
        }
    }

    /**
     * add a life to game
     */
    public void addHeartToLivesCollection() {
        if (livesLeft < 4) {
            Renderable heartImg = imageReader.readImage(HEART_IMG_PATH, true);
            Heart heart =
                    new Heart(new Vector2((NUMERIC_LIFE_DIMENSIONS + 5) + (livesLeft * (HEART_SIZE + 2)),
                            (windowDimensions.y() - NUMERIC_LIFE_DIMENSIONS)),
                            new Vector2(HEART_SIZE, HEART_SIZE), heartImg, this);
            this.gameObjects().addGameObject(heart, Layer.UI);
            livesLeft++;
            this.hearts[livesLeft - 1] = heart;
        }
    }

    /**
     * Updates the game state.
     * @param deltaTime Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfGameEnds();
    }


    /**
     * check if one of the conditions to end the game are true
     */
    private void checkIfGameEnds() {
        // check lose
        ifLose();
        // check win
        ifWin();
    }

    /**
     * check if player wined the game, ask if play again and close or reset game according to players answer
     */
    private void ifWin() {
        if (bricksLeft == 0) {
            if (this.windowController.openYesNoDialog(WIN_MESSAGE)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * check if player lost the game, ask if play again and close or reset game according to players answer
     */
    private void ifLose(){
        float ballHeight = this.ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            if (livesLeft > 1) {
                this.gameObjects().removeGameObject(hearts[livesLeft - 1], Layer.UI);
                hearts[livesLeft - 1] = null;
                livesLeft--;
                updateLifeNumericCounter();
                resetBall();
            } else {
                if (this.windowController.openYesNoDialog(LOSE_MESSAGE)) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            }
        }
    }

    /**
     * update numeric lives counter according to number of lives player gained/lost
     */
    public void updateLifeNumericCounter() {
        lifeNumericCounter.setString(Integer.toString(livesLeft));
        if (livesLeft >= 3){
            lifeNumericCounter.setColor(Color.green);
        } else if (livesLeft == 2){
            lifeNumericCounter.setColor(Color.yellow);
        } else {
            lifeNumericCounter.setColor(Color.red);
        }

    }

    /**
     * reset ball to the beginning position of
     */
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY= BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Removes an object from the game.
     * @param object The object to remove.
     */
    public void removeObjectFromGameObject(GameObject object){
        this.gameObjects().removeGameObject(object);
    }

    /**
     * Adds an object to the game.
     * @param object The object to add.
     */
    public void addObjectToGameObjects(GameObject object){
        this.gameObjects().addGameObject(object);
    }

    /**
     * The bricker.main method for running the game.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        BrickerGameManager manager;
        if (args.length < USER_ARGS) {
            manager = new BrickerGameManager("Bricker", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT),
                    DEFAULT_BRICK_LINES_NUM, DEFAULT_BRICKS_PER_LINE);
        } else {
            manager = new BrickerGameManager("Bricker", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT),
                    Integer.parseInt(args[BRICK_LINES_NUM_ARG]), Integer.parseInt(args[BRICKS_PER_LINE_ARG]));
        }
        manager.run();
    }
}
