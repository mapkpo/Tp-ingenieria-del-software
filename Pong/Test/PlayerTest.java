import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testRightPressed() {
        Player player = new Player(5, 5);
        player.rightPressed();
        // Assuming the initial x position was 0
        assertEquals(6, player.x);
    }

    @Test
    public void testLeftPressed() {
        Player player = new Player(5, 5);
        player.leftPressed();
        // Assuming the initial x position was 0
        assertEquals(4, player.x);
    }

    @Test
    public void testInitialPosition() {
        Player player = new Player(5, 5);
        // Check if the initial position is set correctly
        assertEquals(5, player.x);
        assertEquals(5, player.y);
    }

    @Test
    public void testTickWithinBounds() {
        // Player within bounds, no change expected
        Player player = new Player(10, 0);
        player.tick();
        assertEquals(10, player.x);
    }

    @Test
    public void testTickAtRightBoundary() {
        // Player at right boundary, should move left by 1
        Player player = new Player(Game.WIDTH - Game.player.WIDTH + 1, 0);
        player.tick();
        assertEquals(Game.WIDTH - player.WIDTH, player.x);
    }

    @Test
    public void testTickAtLeftBoundary() {
        // Player at left boundary, should move right by 1
        Player player = new Player(-1, 0);
        player.tick();
        assertEquals(0, player.x);
    }

}