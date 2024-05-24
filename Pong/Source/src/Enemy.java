import java.awt.Color;
import java.awt.Graphics;

/**
 * Enemy class represents an enemy in the game.
 * It extends the Player class and overrides some of its methods.
 */
public class Enemy extends Player{
    
    // X-Y coordinates of the enemy
    public double x;
    public int y;
    // Difficulty level of the enemy between 0.0 and 1.0, where 1 is perfect ball tracking
    private final double DIFFICULTY = 0.7;
    

    /**
     * @brief Constructor for Enemy class.
	 * 
     * @param x Initial x-coordinate of the enemy
     * @param y Initial y-coordinate of the enemy
     */
    Enemy(int x, int y) {
        super(x, y);
    }
    

    /**
     * @brief Method to handle the enemy's position.
	 * 
     * It sets the x-coordinate based on the ball's x-coordinate and the difficulty level.
     * It also checks if the enemy is within the game width and adjusts the x-coordinate accordingly.
     */
    public void tick() {
        x = Game.ball.x * DIFFICULTY;
        
        while(x > Game.WIDTH - super.WIDTH) {
            x--;
        }
        
        while(x < 0) {
            x++;
        }
    }
    

    /**
     * @brief Method to render the enemy on the screen.
	 * 
     * It calls the tick method and then sets the color and fills a rectangle representing the enemy.
     * 
	 * @param g Graphics object used for rendering
     */
    public void render(Graphics g) {
        tick();
        g.setColor(new Color(255, 255, 255));
        g.fillRect((int)x, super.y, super.WIDTH, super.HEIGHT);
    }

}