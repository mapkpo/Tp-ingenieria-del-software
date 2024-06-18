import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @brief Interface for defining speed strategy.
 * 
 * This interface defines a method for retrieving the speed value
 * based on different speed strategy implementations.
 */
interface SpeedStrategy {
    /**
     * @brief Retrieves the speed value.
     * 
     * @return The speed value determined by the strategy.
     */
    double getSpeed();
}

/**
 * @brief Represents an initial speed strategy.
 * 
 * This strategy returns a fixed initial speed value.
 */
class InitialSpeedStrategy implements SpeedStrategy {
    /**
     * @brief Retrieves the initial speed value.
     * 
     * @return The initial speed value, which is 0.7.
     */
    @Override
    public double getSpeed() {
        return 0.7;
    }
}

/**
 * @brief Represents an increased speed strategy.
 * 
 * This strategy returns a medium speed value.
 */
class IncreasedSpeedStrategy implements SpeedStrategy {
    /**
     * @brief Retrieves the medium speed value.
     * 
     * @return The medium speed value, which is 1.0.
     */
    @Override
    public double getSpeed() {
        return 1.0;
    }
}

/**
 * @brief Represents a maximum speed strategy.
 * 
 * This strategy returns a maximum speed value.
 */
class MaxSpeedStrategy implements SpeedStrategy {
    /**
     * @brief Retrieves the maximum speed value.
     * 
     * @return The maximum speed value, which is 1.3.
     */
    @Override
    public double getSpeed() {
        return 1.3;
    }
}

/**
 * @brief Represents a ball in a game.
 * 
 * The ball moves within the game area, interacts with paddles, 
 * and tracks scores.
 */
public class Ball {

    public double x; /**< X-coordinate of the ball's center. */
    public double y; /**< Y-coordinate of the ball's center. */
    public double dx; /**< Velocity component in the x-direction.*/
    public double dy; /**< Velocity component in the y-direction. */
    private Color color; /**< Color of the ball.*/

    private double angle; /**< Angle of movement in degrees. */

    private SpeedStrategy speedStrategy; /**< Strategy for determining the ball's speed. */
    public final int WIDTH = 5; /**< Width of the ball. */
    public final int HEIGHT = 5; /**< Height of the ball. */

    private int playerScore = 0; /**< Player's score. */
    private int enemyScore = 0; /**< Enemy's score. */
    private int lastTotalScore = 0; /**< Last total score recorded. */

    private List<ScoreObserver> observers = new ArrayList<>(); /**< List of score observers. */

    /**
     * @brief Constructs a new Ball object.
     * 
     * Initializes the ball's position, speed strategy, initial angle, and default color.
     */
    public Ball() {
        this.x = Game.WIDTH / 2;
        this.y = Game.HEIGHT / 2;
        this.speedStrategy = new InitialSpeedStrategy(); // initial strategy 
        initializeAngle();
        this.color = Color.WHITE; //default color
    }


    /**
     * @brief Initializes the angle of movement for the ball.
     *
     * The angle is randomly generated within a specific range to ensure varied movement.
     */
    public void initializeAngle() {
        angle = new Random().nextInt(100 - 80 + 1) + 80;

        while (angle < 100 && angle > 80) {
            angle = new Random().nextInt(100 - 80 + 1) + 80;
        }

        this.dx = Math.sin(Math.toRadians(angle));
        this.dy = Math.cos(Math.toRadians(angle));
    }

    /**
     * @brief Updates the position and behavior of the ball in the game.
     * 
     * This method is called in each game tick to move the ball, check for collisions,
     * and handle scoring.
     */
    public void tick() {
        updatePosition();
        checkWallCollision();
        checkPaddleCollision();
        checkScoring();
    }

    /**
     * @brief Updates the position of the ball based on its velocity.
     */
    public void updatePosition() {
        x += dx * speedStrategy.getSpeed();
        y += dy * speedStrategy.getSpeed();
    }

    /**
     * @brief Checks for collision with the side walls of the game window.
     * 
     * If the ball hits the side walls, its horizontal direction is reversed.
     */
    public void checkWallCollision() {
        if (x <= 0 || x >= Game.WIDTH - WIDTH) {
            dx *= -1;
        }
    }

    /**
     * @brief Checks for collision with player and enemy paddles.
     * 
     * If the ball hits a paddle, its angle of movement is adjusted.
     */
    public void checkPaddleCollision() {
        Rectangle bounds = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
        Rectangle boundsPlayer = new Rectangle(Game.player.x, Game.player.y, Game.player.WIDTH, Game.player.HEIGHT);
        Rectangle boundsEnemy = new Rectangle((int) Game.enemy.x, Game.enemy.y, Game.player.WIDTH, Game.player.HEIGHT);

        if (bounds.intersects(boundsPlayer)) {
            adjustAngleAfterPaddleCollision(false);
        } else if (bounds.intersects(boundsEnemy)) {
            adjustAngleAfterPaddleCollision(true);
        }
    }

    /**
     * @brief Adjusts the angle of the ball after a collision with a paddle.
     * 
     * The angle is randomly adjusted to ensure varied movement.
     * 
     * @param hitByEnemy Indicates if the ball was hit by the enemy paddle.
     */
    private void adjustAngleAfterPaddleCollision(boolean hitByEnemy) {
        angle = new Random().nextInt(120 - 60) + 61;

        while (angle < 110 && angle > 70) {
            angle = new Random().nextInt(120 - 60) + 61;
        }

        this.dx = Math.sin(Math.toRadians(angle));
        this.dy = Math.cos(Math.toRadians(angle));

        if (hitByEnemy && dy < 0) {
            dy *= -1;
        } else if (!hitByEnemy && dy > 0) {
            dy *= -1;
        }
    }

    /**
     * @brief Checks for scoring events and handles them accordingly.
     * 
     * If the ball goes out of bounds, the appropriate player is awarded a point,
     * and the ball is reset.
     */
    public void checkScoring() {
        if (y >= Game.HEIGHT) {
            enemyScore++;
            notifyObservers();
            resetBall();
        } else if (y <= 0) {
            playerScore++;
            notifyObservers();
            resetBall();
        }

        // Change speed strategy every 3 points
        int totalScore = playerScore + enemyScore;
        if (totalScore > lastTotalScore && totalScore % 3 == 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(3) + 1;
            changeSpeedStrategy(randomNumber);
            lastTotalScore = totalScore; // update the last total score
        }
    }
    
    /**
     * @brief Adds an observer from the list of observers.
     * 
     * @param observer The observer to add.
     */
    public void addObserver(ScoreObserver observer) {
        observers.add(observer);
    }

    /**
     * @brief Removes an observer from the list of observers.
     * 
     * @param observer The observer to remove.
     */
    public void removeObserver(ScoreObserver observer) {
        observers.remove(observer);
    }

    /**
     * @brief Notifies all observers of a score update.
     * 
     * It calls the updateScore method for each observer in the list of observers.
     */
    private void notifyObservers() {
        for (ScoreObserver observer : observers) {
            observer.updateScore(playerScore, enemyScore);
        }
    }

    /**
     * @brief Resets the ball to its initial position and angle.
     * 
     * The ball is reset to the center of the game window and a new angle of movement is generated.
     */
    private void resetBall() {
        this.x = Game.WIDTH / 2;
        this.y = Game.HEIGHT / 2;
        initializeAngle();
    }

    /**
     * @brief Renders the ball on the screen.
     * 
     * It calls the tick method and then sets the color and fills a rectangle representing the ball.
     * 
     * @param g Graphics object used for rendering.
     */
    public void render(Graphics g) {
        tick();
        g.setColor(this.color);
        g.fillRect((int) x, (int) y, WIDTH, HEIGHT);
    }

    /**
     * @brief Gets the player's score.
     * 
     * @return The player's score.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * @brief Gets the enemy's score.
     * 
     * @return The enemy's score.
     */
    public int getEnemyScore() {
        return enemyScore;
    }

    /**
     * @brief Changes the ball's speed strategy based on the given level.
     * 
     * @param level The level of speed strategy to apply (1 for Initial, 2 for Increased, 3 for Max).
     */
    private void changeSpeedStrategy(int level) {
        switch (level) {
            case 1:
                this.speedStrategy = new InitialSpeedStrategy();
                setColor(Color.WHITE);
                break;
            case 2:
                this.speedStrategy = new IncreasedSpeedStrategy();
                setColor(Color.YELLOW);
                break;
            case 3:
                this.speedStrategy = new MaxSpeedStrategy();
                setColor(Color.RED);
                break;
        }
    }

    /**
     * @brief change ball color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}