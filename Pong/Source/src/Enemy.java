import java.awt.Color;
import java.awt.Graphics;

/**
 * @brief Represents an enemy in the Pong game.
 * 
 * The Enemy class extends the Player class and overrides some of its methods to implement
 * specific behavior for the enemy player. The enemy's position is updated based on the ball's
 * position and a difficulty level.
 */
public class Enemy extends Player {
    
    public double x;                        /**< @brief The x-coordinate of the enemy. */
    public int y;                           /**< @brief The y-coordinate of the enemy. */
    private final double DIFFICULTY = 0.7;  /**< @brief Difficulty level of the enemy (between 0.0 and 1.0), where 1 is perfect ball tracking. */

    /**
     * @brief Constructs a new Enemy.
     * 
     * @param x Initial x-coordinate of the enemy.
     * @param y Initial y-coordinate of the enemy.
     */
    Enemy(int x, int y) {
        super(x, y);
    }

    /**
     * @brief Updates the position of the enemy.
     * 
     * The x-coordinate of the enemy is set based on the ball's x-coordinate and the difficulty level.
     * It ensures that the enemy stays within the game boundaries.
     */
    public void tick() {
        x = Game.ball.x * DIFFICULTY;

        while (x > Game.WIDTH - super.WIDTH) {
            x--;
        }

        while (x < 0) {
            x++;
        }
    }

    /**
     * @brief Renders the enemy on the screen.
     * 
     * It calls the tick method to update the position and then sets the color and fills a rectangle representing the enemy.
     * 
     * @param g Graphics object used for rendering.
     */
    public void render(Graphics g) {
        tick();
        g.setColor(new Color(255, 255, 255));
        g.fillRect((int)x, super.y, super.WIDTH, super.HEIGHT);
    }
}
