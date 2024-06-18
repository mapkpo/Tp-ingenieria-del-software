import org.junit.Test;
import org.mockito.Mockito;
import java.awt.Graphics;
import java.awt.Color;
import static org.junit.Assert.*;

public class EnemyTest {

    @Test
    public void testTick() {
        Game.ball = new Ball();
        Enemy enemy = new Enemy(0, 0);

        // Set the ball's x value to a value that would make the enemy's x value greater than Game.WIDTH - super.WIDTH
        Game.ball.x = (int)((Game.WIDTH - enemy.WIDTH + 1) / 0.7);

        enemy.tick();

        // Check if the enemy's x value has been decreased to Game.WIDTH - super.WIDTH
        assertEquals(Game.WIDTH - enemy.WIDTH, enemy.x, 0.2);

        // Move the ball to a negative x-coordinate and check if the enemy's x-coordinate is correctly adjusted
        Game.ball.x = -10;
        enemy.tick();
        assertEquals(0, enemy.x, 0.2);
    }
    
    @Test
    public void testRender() {
        // Create a mock Graphics object
        Graphics g = Mockito.mock(Graphics.class);

        // Create an Enemy object
        Enemy enemy = new Enemy(0, 0);

        // Call the render method
        enemy.render(g);

        // Verify that setColor was called with the correct color
        Mockito.verify(g).setColor(new Color(255, 255, 255));

        // Verify that fillRect was called with the correct parameters
        Mockito.verify(g).fillRect((int)enemy.x, enemy.y, enemy.WIDTH, enemy.HEIGHT);
    }
}
