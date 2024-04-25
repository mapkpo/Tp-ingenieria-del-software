import java.awt.Color;
import java.awt.Graphics;

/**
 * Player class represents a player in the game.
 * It contains methods to handle player movements and rendering.
 */
public class Player {
    // Width and height of the paddle in pixels
    protected final int WIDTH = 40;
    protected final int HEIGHT = 5;
    
    // X-Y coordinates of the paddle
    protected int x;
    protected int y;
    

    /**
     * @brief Constructor for Player class.
	 * 
     * @param x Initial x-coordinate of the player
     * @param y Initial y-coordinate of the player
     */
    Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    

    /**
     * @brief Method to handle right movement of the player.
	 * 
     * It increments the x-coordinate and calls the tick method.
     */
    public void rightPressed() {
        this.x++;
        tick();
    }
    

    /**
     * @brief Method to handle left movement of the player.
	 * 
     * It decrements the x-coordinate and calls the tick method.
     */
    public void leftPressed() {
        this.x--;
        tick();
    }
    

    /**
     * @brief Method to handle the player's position.
	 * 
     * It checks if the player is within the game width and adjusts the x-coordinate accordingly.
     */
    public void tick() {
        if (x + WIDTH > Game.WIDTH) {
            x--;
        }
        else if(x < 0) {
            x++;
        } 
    }
    

    /**
     * @brief Method to render the player on the screen.
	 * 
     * It sets the color and fills a rectangle representing the player.
	 * 
     * @param g Graphics object used for rendering
     */
    public void render(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
	
}
