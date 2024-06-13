import java.awt.Color;
import java.awt.Graphics;

/**
 * @brief Represents a player in the Pong game.
 * 
 * The Player class handles player movements and rendering. It contains methods to move the player
 * paddle left and right and ensure it stays within the game boundaries.
 */
public class Player {
    
    protected final int WIDTH = 40;     /**< @brief Width of the paddle in pixels. */
    protected final int HEIGHT = 5;     /**< @brief Height of the paddle in pixels. */
    
    protected int x;                    /**< @brief The x-coordinate of the paddle. */
    protected int y;                    /**< @brief The y-coordinate of the paddle. */

    /**
     * @brief Constructs a new Player.
     * 
     * @param x Initial x-coordinate of the player.
     * @param y Initial y-coordinate of the player.
     */
    Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @brief Handles right movement of the player.
     * 
     * Increments the x-coordinate and calls the tick method to update the position.
     */
    public void rightPressed() {
        this.x++;
        tick();
    }

    /**
     * @brief Handles left movement of the player.
     * 
     * Decrements the x-coordinate and calls the tick method to update the position.
     */
    public void leftPressed() {
        this.x--;
        tick();
    }

    /**
     * @brief Updates the player's position.
     * 
     * Ensures the player stays within the game boundaries by adjusting the x-coordinate.
     */
    public void tick() {
        if (x + WIDTH > Game.WIDTH) {
            x--;
        } else if (x < 0) {
            x++;
        }
    }

    /**
     * @brief Renders the player on the screen.
     * 
     * Sets the color and fills a rectangle representing the player.
     * 
     * @param g Graphics object used for rendering.
     */
    public void render(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
