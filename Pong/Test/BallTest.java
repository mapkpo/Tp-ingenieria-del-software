import org.junit.Test;
import org.mockito.Mockito;
import java.awt.Graphics;
import java.awt.Color;
import org.junit.Before;

import static org.junit.Assert.*;

public class BallTest {

    private Ball ball;

    @Before
    public void setUp() {
        // Initialize Game.player and Game.enemy before each test
        Game.player = new Player(100, 155);
        Game.enemy = new Enemy(100, 0);
        ball = new Ball();
    }
    

    @Test
    public void testBallInitialization() {         
        // Check initial position
        assertEquals(Game.WIDTH / 2, ball.x, 0.001);
        assertEquals(Game.HEIGHT / 2, ball.y, 0.001);
        
        // Check initial direction
        assertTrue(ball.dx >= -1 && ball.dx <= 1);
        assertTrue(ball.dy >= -1 && ball.dy <= 1);
    }

    @Test
    public void testBallMovement() {         
        // Save initial position
        double initialX = ball.x;
        double initialY = ball.y;
        
        // Move the ball
        ball.tick();
        
        // Check if position has changed
        assertNotEquals(initialX, ball.x, 0.001);
        assertNotEquals(initialY, ball.y, 0.001);
    }

    @Test
    public void testPlayerCollision() {
        // Set ball position to intersect with player
        ball.x = Game.player.x + Game.player.WIDTH / 2;
        ball.y = Game.player.y - 1;
        
        // Save initial direction
        double initialDx = ball.dx;
        double initialDy = ball.dy;
        
        // Perform collision check
        ball.tick();
        
        // Check if direction changed after collision
        assertNotEquals(initialDx, ball.dx);
        assertNotEquals(initialDy, ball.dy);
    }

    @Test
    public void testEnemyCollision() {         
        // Set ball position to intersect with enemy
        ball.x = Game.enemy.x + Game.enemy.WIDTH / 2;
        ball.y = Game.enemy.y + 1; // Below the enemy paddle
        
        // Save initial direction
        double initialDx = ball.dx;
        double initialDy = ball.dy;
        
        // Perform collision check
        ball.tick();
        
        // Check if direction changed after collision
        assertNotEquals(initialDx, ball.dx);
        assertNotEquals(initialDy, ball.dy);
    }

    @Test
    public void testRightWallCollision() {         
        // Set ball position close to the right wall
        ball.x = Game.WIDTH - ball.WIDTH / 2 - 1; // Position the ball close to the right wall
        ball.y = Game.HEIGHT / 2;
        
        // Ensure ball is moving towards the right wall
        ball.dx = Math.abs(ball.dx); // Ensure dx is positive
        
        // Save initial direction
        double initialDx = ball.dx;
        double initialDy = ball.dy;
        
        // Perform collision check
        ball.tick();
        
        // Check if direction changed after collision
        assertNotEquals(initialDx, ball.dx, 0.001);
        assertEquals(initialDy, ball.dy, 0.001); // Direction along y-axis should not change for side wall collision
    }

    @Test
    public void testRender() {
        // Create a mock Graphics object
        Graphics g = Mockito.mock(Graphics.class);
    
        // Call the render method
        ball.render(g);
    
        // Verify that setColor was called with the correct color
        Mockito.verify(g).setColor(new Color(255, 255, 255));
    
        // Verify that fillRect was called with the correct parameters
        Mockito.verify(g).fillRect((int)ball.x, (int)ball.y, ball.WIDTH, ball.HEIGHT);
    }

    @Test
    public void testCheckScoring() {  
        // Set up mock observer
        ScoreObserver observer = Mockito.mock(ScoreObserver.class);
        ball.addObserver(observer);

        // Test enemy score
        ball.y = Game.HEIGHT + 1;
        ball.checkScoring();
        assertEquals(1, ball.getEnemyScore());
        assertEquals(0, ball.getPlayerScore());
        Mockito.verify(observer).updateScore(0, 1);

        // Test player score
        ball.y = -1;
        ball.checkScoring();
        assertEquals(1, ball.getPlayerScore());
        assertEquals(1, ball.getEnemyScore());
        Mockito.verify(observer).updateScore(1, 1);
    }

    
}
