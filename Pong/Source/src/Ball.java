import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Interfaz para la estrategia de velocidad
interface SpeedStrategy {
    double getSpeed();
}

// initial speed strategy
class InitialSpeedStrategy implements SpeedStrategy {
    @Override
    public double getSpeed() {
        return 0.7;
    }
}

// medium speed strategy
class IncreasedSpeedStrategy implements SpeedStrategy {
    @Override
    public double getSpeed() {
        return 1.0;
    }
}

// max speed strategy
class MaxSpeedStrategy implements SpeedStrategy {
    @Override
    public double getSpeed() {
        return 1.3;
    }
}

public class Ball {

    public double x;
    public double y;
    public double dx;
    public double dy;

    private double angle;

    private SpeedStrategy speedStrategy; // speed strategy 
    public final int WIDTH = 5;
    public final int HEIGHT = 5;

    private int playerScore = 0;
    private int enemyScore = 0;

    private List<ScoreObserver> observers = new ArrayList<>();

    public Ball() {
        this.x = Game.WIDTH / 2;
        this.y = Game.HEIGHT / 2;
        this.speedStrategy = new InitialSpeedStrategy(); // initial strategy 
        initializeAngle();
    }


        /**
     * @brief Initializes the angle of movement for the ball.
     *
     * The angle is randomly generated within a specific range to ensure varied movement.
     */
    public void initializeAngle() {
        angle = new Random().nextInt(120 - 60) + 61;

        while (angle < 110 && angle > 70) {
            angle = new Random().nextInt(120 - 60) + 61;
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
        if (totalScore % 3 == 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(3) + 1;
            changeSpeedStrategy(randomNumber);
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
        g.setColor(new Color(255, 255, 255));
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

    private void changeSpeedStrategy(int level) {
        switch (level) {
            case 1:
                this.speedStrategy = new InitialSpeedStrategy();
                break;
            case 2:
                this.speedStrategy = new IncreasedSpeedStrategy();
                break;
            default:
                this.speedStrategy = new MaxSpeedStrategy();
                break;
        }
    }
}