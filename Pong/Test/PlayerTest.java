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
}