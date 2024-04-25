import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BallTest {

    @Before
    public void setUp() {
        // Initialize Game.player and Game.enemy before each test
        Game.player = new Player(100, 155);
        Game.enemy = new Enemy(100, 0);
    }
    

    @Test
    public void testBallInitialization() {
        Ball ball = new Ball();
        
        // Check initial position
        assertEquals(Game.WIDTH / 2, ball.x, 0.001);
        assertEquals(40, ball.y, 0.001);
        
        // Check initial direction
        assertTrue(ball.dx >= -1 && ball.dx <= 1);
        assertTrue(ball.dy >= -1 && ball.dy <= 1);
    }

    @Test
    public void testBallMovement() {
        Ball ball = new Ball();
        
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
        // Initialize the ball
        Ball ball = new Ball();
        
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
    public void testRightWallCollision() {
        // Initialize the ball
        Ball ball = new Ball();
        
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
    
}
